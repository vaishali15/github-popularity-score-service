package com.challenge.github.popularityscore.service;

import com.challenge.github.popularityscore.dto.egress.PageResponseDto;
import com.challenge.github.popularityscore.dto.egress.RepositoryResponseDto;
import com.challenge.github.popularityscore.dto.ingress.GithubRepositoryItemDto;
import com.challenge.github.popularityscore.dto.ingress.GithubSearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * Calls GitHub Search API and maps results to popularity score DTOs.
 */
@Service
@RequiredArgsConstructor
public class GithubRepositorySearchService {

    private final PopularityScoreCalculator calculator;
    private final RestClient.Builder githubRestClientBuilder;

    public PageResponseDto<RepositoryResponseDto> search(String language, String createdAfter, int page, int size) {
        String query = "language:%s created:>=%s".formatted(language, createdAfter);
        var githubRestClient = githubRestClientBuilder.build();
        var response = githubRestClient.get()
            .uri(u -> u.path("/search/repositories")
                .queryParam("q", query)
                .queryParam("sort", "stars")
                .queryParam("order", "desc")
                .queryParam("page", page)
                .queryParam("per_page", size)
                .build())
            .retrieve()
            .body(GithubSearchResponseDto.class);

        if (response == null || response.items() == null) {
            return new PageResponseDto<>(page, size, 0, 0, List.of());
        }

        var now = OffsetDateTime.now();
        var items = response.items().stream()
            .map(item -> toResponse(item, now))
            .sorted(Comparator.comparingDouble(RepositoryResponseDto::popularityScore).reversed())
            .toList();

        return new PageResponseDto<>(page, size, items.size(), response.totalCount(), items);
    }

    private RepositoryResponseDto toResponse(GithubRepositoryItemDto githubRepositoryItemDto, OffsetDateTime now) {
        var score = calculator.calculate(githubRepositoryItemDto, now).score();
        return new RepositoryResponseDto(
            githubRepositoryItemDto.fullName(), githubRepositoryItemDto.htmlUrl(), githubRepositoryItemDto.stargazersCount(), githubRepositoryItemDto.forksCount(), githubRepositoryItemDto.updatedAt(), score
        );
    }
}
