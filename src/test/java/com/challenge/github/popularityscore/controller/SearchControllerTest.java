package com.challenge.github.popularityscore.controller;


import com.challenge.github.popularityscore.dto.ApiSearchResponseDto;
import com.challenge.github.popularityscore.mapper.ApiSearchResponseMapper;
import com.challenge.github.popularityscore.mapper.ApiSearchResponseMapperImpl;
import com.challenge.github.popularityscore.mapper.RepositoryMapperImpl;
import com.challenge.github.popularityscore.model.ApiSearchResponse;
import com.challenge.github.popularityscore.model.Repository;
import com.challenge.github.popularityscore.service.RepositorySearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static java.time.OffsetDateTime.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchController.class)
@Import({ApiSearchResponseMapperImpl.class, RepositoryMapperImpl.class})
public class SearchControllerTest {

    private static final String SEARCH_URL = "/api/v1/repositories/search";
    private static final OffsetDateTime SAMPLE_TIME = parse("2025-10-01T00:00:00Z");

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    RepositorySearchService service;

    @Autowired
    ApiSearchResponseMapper apiSearchResponseMapper;

    @Test
    void searchRepositories() throws Exception {
        var item = Repository.builder()
            .repositoryName("test/repo")
            .repositoryUrl("https://github.com/test/repo")
            .stars(10)
            .forks(2)
            .lastUpdated(parse("2025-10-01T00:00:00Z"))
            .popularityScore(123.45)
            .build();
        var apiSearchResponse = ApiSearchResponse.builder()
            .page(1)
            .size(2)
            .total(1)
            .totalAvailable(100)
            .gitHubRepoMetaList(List.of(item))
            .build();

        when(service.search("Java", "2024-01-01", 1, 2)).thenReturn(apiSearchResponse);

        var mvcResult = mvc.perform(get(SEARCH_URL)
                .param("language", "Java")
                .param("createdAfter", "2024-01-01")
                .param("page", "1")
                .param("size", "2"))
            .andExpect(status().isOk())
            .andReturn();

        var body = mvcResult.getResponse().getContentAsString();
        var dto = mapper.readValue(body, ApiSearchResponseDto.class);

        assertThat(dto.totalAvailable()).isEqualTo(100);
        assertThat(dto.gitHubRepoMetaList()).isNotEmpty();
    }

    @Test
    void invalidRequestParams() throws Exception {
        mvc.perform(get(SEARCH_URL)
                .param("language", "")
                .param("createdAfter", "bad")
                .param("page", "0")
                .param("size", "0"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"));
    }
}
