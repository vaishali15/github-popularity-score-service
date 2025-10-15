package com.challenge.github.popularityscore.dto.ingress;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Root response from the GitHub Search Repositories API.
 */
@Jacksonized
@Builder
public record GithubSearchResponseDto(
    int totalCount,
    boolean incompleteResults,
    List<GithubRepositoryItemDto> items
) {
}
