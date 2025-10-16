package com.challenge.github.popularityscore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.OffsetDateTime;

/**
 * API response model for a repository with its popularity score.
 */

@Builder
@Schema(name = "Repository",
    description = "Repository summary with a computed popularity score.")
public record GitHubRepoMetaDto(
    @Schema(description = "Repository full name", example = "spring-projects/spring-boot")
    String repositoryName,

    @Schema(description = "Repository URL", example = "https://github.com/spring-projects/spring-boot", format = "uri")
    String repositoryUrl,

    @Schema(description = "Stars count", example = "72000")
    int stars,

    @Schema(description = "Forks count", example = "43000")
    int forks,

    @Schema(description = "Last update time", example = "2025-09-01T12:34:56Z", format = "date-time")
    OffsetDateTime lastUpdated,

    @Schema(description = "Computed popularity score", example = "50432.7")
    double popularityScore) {
}
