package com.challenge.github.popularityscore.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Popularity scoring weights configuration properties.
 * <p>
 * Values are loaded from {@code app.scoring.*} in the application configuration.
 *
 * @param starsWeight         weight applied to stars count
 * @param forksWeight         weight applied to fork count
 * @param recencyWeight       weight applied to recency of updates
 * @param recencyHalfLifeDays half-life in days for exponential recency decay
 */
@ConfigurationProperties(prefix = "app.scoring")
public record ScoringProperties(double starsWeight,
                                double forksWeight,
                                double recencyWeight,
                                int recencyHalfLifeDays) {
}
