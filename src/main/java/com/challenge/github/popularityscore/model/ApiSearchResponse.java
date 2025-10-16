package com.challenge.github.popularityscore.model;

import lombok.Builder;

import java.util.List;

@Builder
public record ApiSearchResponse(
    int page,
    int size,
    int total,
    int totalAvailable,
    List<Repository> gitHubRepoMetaList) {
}
