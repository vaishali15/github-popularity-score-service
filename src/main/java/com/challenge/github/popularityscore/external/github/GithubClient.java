package com.challenge.github.popularityscore.external.github;

import com.challenge.github.popularityscore.external.github.dto.GithubSearchResponseDto;
import com.challenge.github.popularityscore.external.github.mapper.GithubSearchResponseMapper;
import com.challenge.github.popularityscore.external.github.model.GithubSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Calls GitHub  Search API and return result.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GithubClient {

    private final RestClient githubRestClient;
    private final GithubSearchResponseMapper mapper;

    public GithubSearchResponse search(String language, String createdAfter, int page, int size) {
        var query = "language:%s created:>=%s".formatted(language, createdAfter);
        try {
            var githubSearchResponseDto = githubRestClient.get()
                .uri(u -> u.path("/search/repositories")
                    .queryParam("q", query)
                    .queryParam("sort", "stars")
                    .queryParam("order", "desc")
                    .queryParam("page", page)
                    .queryParam("per_page", size)
                    .build())
                .retrieve()
                .body(GithubSearchResponseDto.class);
            return mapper.toModel(githubSearchResponseDto);
        } catch (Exception e) {
            log.error("Github Api failed", e);
            throw e;
        }
    }
}
