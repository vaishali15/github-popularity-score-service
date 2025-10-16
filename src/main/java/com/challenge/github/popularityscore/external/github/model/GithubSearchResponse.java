package com.challenge.github.popularityscore.external.github.model;

import lombok.Builder;

import java.util.List;

/**
 * Model Bean for the GitHub Search Repositories API.
 */
@Builder
public record GithubSearchResponse(
    int totalCount,
    boolean incompleteResults,
    List<GithubRepositoryItem> items
) {
}
