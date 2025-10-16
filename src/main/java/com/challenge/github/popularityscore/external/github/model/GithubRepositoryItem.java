package com.challenge.github.popularityscore.external.github.model;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record GithubRepositoryItem(
    String fullName,
    String htmlUrl,
    int stargazersCount,
    int forksCount,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    String language
) {
}
