package com.challenge.github.popularityscore.controller;

import com.challenge.github.popularityscore.dto.ApiSearchResponseDto;
import com.challenge.github.popularityscore.mapper.ApiSearchResponseMapper;
import com.challenge.github.popularityscore.service.RepositorySearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Search GitHub repositories with popularity scores.
 */
@Tag(
    name = "GitHub Repository Search",
    description = "Search GitHub repositories and return popularity score."
)
@RestController
@RequestMapping("/api/v1/repositories")
@Validated
@RequiredArgsConstructor
public class SearchController {

    private final RepositorySearchService searchService;
    private final ApiSearchResponseMapper apiSearchResponseMapper;

    @Operation(
        summary = "Search GitHub Repositories and compute popularity score",
        description = "Filters repositories by language and creation date, sorts by stars, and returns results ranked by popularity score."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(mediaType = "application/problem+json")),
        @ApiResponse(responseCode = "500", description = "Server error",
            content = @Content(mediaType = "application/problem+json"))
    })
    @GetMapping("/search")
    public ApiSearchResponseDto search(
        @Parameter(description = "Repository primary language", example = "Java") @RequestParam @NotBlank String language,
        @Parameter(description = "Repository creation date (YYYY-MM-DD)", example = "2024-01-01")
        @RequestParam @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "YYYY-MM-DD") String createdAfter,
        @Parameter(description = "Page index", example = "1") @RequestParam(defaultValue = "1") @Min(1) int page,
        @Parameter(description = "Page size (max 100)", example = "10") @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return apiSearchResponseMapper.toDto(searchService.search(language, createdAfter, page, size));
    }
}
