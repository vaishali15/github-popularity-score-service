package com.challenge.github.popularityscore.model;

import lombok.Builder;

/**
 * Computed popularity score.
 */
@Builder
public record PopularityScore(double score) {
}
