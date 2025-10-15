package com.challenge.github.popularityscore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Entry point for the GitHub Popularity Score Service application.
 * <p>
 * Provides REST endpoints to search GitHub repositories and calculate
 * popularity scores based on stars, forks, and recency of updates.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class GitHubPopularityScoreServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GitHubPopularityScoreServiceApplication.class, args);
    }
}
