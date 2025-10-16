package com.challenge.github.popularityscore.external.github.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.time.OffsetDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record GithubRepositoryItemDto(
    String fullName,
    String htmlUrl,
    int stargazersCount,
    int forksCount,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    String language
) {
}
