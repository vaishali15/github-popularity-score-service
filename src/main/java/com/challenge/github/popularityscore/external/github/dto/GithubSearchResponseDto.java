package com.challenge.github.popularityscore.external.github.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.util.List;

/**
 * Root response from the GitHub Search Repositories API.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record GithubSearchResponseDto(
    int totalCount,
    boolean incompleteResults,
    List<GithubRepositoryItemDto> items
) {
}
