package com.challenge.github.popularityscore.service;

import com.challenge.github.popularityscore.external.github.GithubClient;
import com.challenge.github.popularityscore.external.github.model.GithubRepositoryItem;
import com.challenge.github.popularityscore.model.ApiSearchResponse;
import com.challenge.github.popularityscore.model.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * Calls GitHub client and maps results to api response model bean.
 */
@Service
@RequiredArgsConstructor
public class RepositorySearchServiceImpl implements RepositorySearchService {

    private final PopularityScoreCalculator calculator;
    private final GithubClient githubClient;

    public ApiSearchResponse search(String language, String createdAfter, int page, int size) {
        var response = githubClient.search(language, createdAfter, page, size);

        if (response == null || response.items() == null) {
            return ApiSearchResponse.builder()
                .page(page)
                .size(size)
                .total(0)
                .totalAvailable(0)
                .gitHubRepoMetaList(List.of())
                .build();
        }

        var items = response.items().stream()
            .map(item -> toResponse(item, OffsetDateTime.now()))
            .sorted(Comparator.comparingDouble(Repository::popularityScore).reversed())
            .toList();

        return ApiSearchResponse.builder()
            .page(page)
            .size(size)
            .total(items.size())
            .totalAvailable(response.totalCount())
            .gitHubRepoMetaList(items)
            .build();
    }

    private Repository toResponse(GithubRepositoryItem githubRepositoryItem, OffsetDateTime now) {
        var score = calculator.calculate(githubRepositoryItem, now);
        return Repository.builder()
            .repositoryName(githubRepositoryItem.fullName())
            .repositoryUrl(githubRepositoryItem.htmlUrl())
            .stars(githubRepositoryItem.stargazersCount())
            .forks(githubRepositoryItem.forksCount())
            .lastUpdated(githubRepositoryItem.updatedAt())
            .popularityScore(score)
            .build();
    }
}
