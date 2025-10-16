package com.challenge.github.popularityscore.it;

import com.challenge.github.popularityscore.dto.ApiSearchResponseDto;
import com.challenge.github.popularityscore.dto.GitHubRepoMetaDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RepositorySearchIT {

    private static final WireMockServer wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
    @Autowired
    TestRestTemplate http;
    @Autowired
    ObjectMapper mapper;

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        if (!wireMockServer.isRunning()) wireMockServer.start();
        r.add("app.github.base-url", () -> "http://localhost:" + wireMockServer.port());
        r.add("app.github.token", () -> "");
    }

    @AfterAll
    static void stop() {
        wireMockServer.stop();
    }

    @Test
    void validationErrors() {
        var resp = http.getForEntity(
            "/api/v1/repositories/search?language=&createdAfter=bad&page=0&size=0",
            String.class
        );
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getHeaders().getContentType()).isEqualByComparingTo(MediaType.APPLICATION_PROBLEM_JSON);
    }

    @Test
    void searchRepositories() throws Exception {
        wireMockServer.stubFor(get(urlPathEqualTo("/search/repositories"))
            .withQueryParam("q", matching("language:Java created:>=2024-01-01"))
            .withQueryParam("sort", equalTo("stars"))
            .withQueryParam("order", equalTo("desc"))
            .withQueryParam("page", equalTo("1"))
            .withQueryParam("per_page", equalTo("2"))
            .willReturn(okJson("""
                  {
                    "total_count": 100,
                    "gitHubRepoMetaList": [
                      {
                        "full_name": "test/repo",
                        "html_url": "https://github.com/test/repo",
                        "stargazers_count": 10,
                        "forks_count": 2,
                        "language": "Java",
                        "updated_at": "2025-10-01T00:00:00Z"
                      }
                    ]
                  }
                """)));

        var resp = http.getForEntity(
            "/api/v1/repositories/search?language=Java&createdAfter=2024-01-01&page=1&size=2",
            String.class
        );

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Deserialize using app’s ObjectMapper (handles SNAKE_CASE globally)
        var type = mapper.getTypeFactory()
            .constructParametricType(ApiSearchResponseDto.class, GitHubRepoMetaDto.class);
        ApiSearchResponseDto apiSearchResponseDto = mapper.readValue(resp.getBody(), type);

        assertThat(apiSearchResponseDto.page()).isEqualTo(1);
        assertThat(apiSearchResponseDto.total()).isEqualTo(1);
        assertThat(apiSearchResponseDto.gitHubRepoMetaList()).hasSize(1);
        assertThat(apiSearchResponseDto.gitHubRepoMetaList().getFirst().repositoryName()).isEqualTo("test/repo");
        assertThat(apiSearchResponseDto.gitHubRepoMetaList().getFirst().popularityScore()).isGreaterThan(0.0);
    }

    @Test
    void emptyResult() throws Exception {
        wireMockServer.stubFor(get(urlPathEqualTo("/search/repositories"))
            .willReturn(okJson("""
                  {"total_count":0,"gitHubRepoMetaList":[]}
                """)));

        var resp = http.getForEntity(
            "/api/v1/repositories/search?language=Java&createdAfter=2024-01-01&page=1&size=5",
            String.class
        );

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat(resp.getBody()).contains("\"gitHubRepoMetaList\":[]");
    }
}
