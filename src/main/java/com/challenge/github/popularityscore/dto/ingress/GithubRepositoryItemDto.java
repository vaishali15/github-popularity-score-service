package com.challenge.github.popularityscore.dto.ingress;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.time.OffsetDateTime;

@Jacksonized
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
