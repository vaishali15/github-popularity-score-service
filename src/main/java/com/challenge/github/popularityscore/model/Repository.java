package com.challenge.github.popularityscore.model;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record Repository(
    String repositoryName,
    String repositoryUrl,
    int stars,
    int forks,
    OffsetDateTime lastUpdated,
    double popularityScore) {
}
