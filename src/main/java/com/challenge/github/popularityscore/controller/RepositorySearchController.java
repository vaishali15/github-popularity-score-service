package com.challenge.github.popularityscore.controller;

import com.challenge.github.popularityscore.dto.egress.PageResponseDto;
import com.challenge.github.popularityscore.dto.egress.RepositoryResponseDto;
import com.challenge.github.popularityscore.service.GithubRepositorySearchService;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST endpoint: search GitHub repositories with popularity scores.
 */
@RestController
@RequestMapping("/api/v1/repositories")
@Validated
@RequiredArgsConstructor
public class RepositorySearchController {

    private final GithubRepositorySearchService service;

    @GetMapping("/search")
    public PageResponseDto<RepositoryResponseDto> search(
        @RequestParam @NotBlank String language,
        @RequestParam @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "YYYY-MM-DD") String createdAfter,
        @RequestParam(defaultValue = "1") @Min(1) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return service.search(language, createdAfter, page, size);
    }
}
