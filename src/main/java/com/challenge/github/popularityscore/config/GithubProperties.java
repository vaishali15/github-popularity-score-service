package com.challenge.github.popularityscore.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * GitHub API configuration properties.
 * <p>
 * Values are loaded from {@code app.github.*} in the application configuration.
 *
 * @param baseUrl GitHub REST API base URL (e.g., https://api.github.com)
 * @param token   optional personal access token for higher rate limits
 */

@Validated
@ConfigurationProperties(prefix = "app.github")
public record GithubProperties(
    @NotBlank String baseUrl,
    String token
) {
}

