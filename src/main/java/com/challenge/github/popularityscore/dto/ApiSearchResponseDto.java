package com.challenge.github.popularityscore.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

/**
 * Generic paginated response wrapper.
 *
 */
@Builder
@Schema(
    name = "Repository Search Response ",
    description = "Repository Search Response with GitHub repository details."
)
public record ApiSearchResponseDto(
    @Schema(description = "Requested page index.", example = "1")
    int page,

    @Schema(description = "Requested page size.", example = "10")
    int size,

    @Schema(description = "Number of GitHub repositories in this page.", example = "10")
    int total,

    @Schema(description = "Total repositories available on GitHub.", example = "123456")
    int totalAvailable,

    @ArraySchema(arraySchema = @Schema(description = "List of GitHub repositories"))
    List<GitHubRepoMetaDto> gitHubRepoMetaList) {
}
