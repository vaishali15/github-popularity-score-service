package com.challenge.github.popularityscore.dto.egress;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.time.OffsetDateTime;

/**
 * API response model for a repository with its popularity score.
 */
@Jacksonized
@Builder
public record RepositoryResponseDto(String repositoryName,
                                    String repositoryUrl,
                                    int stars,
                                    int forks,
                                    OffsetDateTime lastUpdated,
                                    double popularityScore) {
}
