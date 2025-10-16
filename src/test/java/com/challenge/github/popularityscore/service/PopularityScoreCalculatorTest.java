package com.challenge.github.popularityscore.service;

import com.challenge.github.popularityscore.config.ScoreConfig;
import com.challenge.github.popularityscore.external.github.model.GithubRepositoryItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.OffsetDateTime;

import static java.time.OffsetDateTime.parse;
import static org.assertj.core.api.Assertions.assertThat;

class PopularityScoreCalculatorTest {

    private static final OffsetDateTime NOW = parse("2025-10-15T12:00:00Z");

    private final PopularityScoreCalculator calculator =
        new PopularityScoreCalculator(new ScoreConfig(0.7, 0.2, 0.1, 30));

    private static GithubRepositoryItem repo(String name, int stars, int forks, int daysAgo) {
        return GithubRepositoryItem.builder()
            .fullName(name)
            .htmlUrl("https://github.com/" + name)
            .stargazersCount(stars)
            .forksCount(forks)
            .language("Java")
            .updatedAt(NOW.minusDays(daysAgo))
            .build();
    }

    @Test
    void scoreIsNonNegative() {
        var repo = repo("test/repo", 0, 0, 365);
        var score = calculator.calculate(repo, NOW);
        assertThat(score).isGreaterThanOrEqualTo(0.0);
    }


    @ParameterizedTest(name = "[{index}] Repository 1(★ : {0} ,forks : {1} ,lastUpdatedDaysAgo : {2}) -> Repository 2(★ : {3} ,forks : {4} ,lastUpdatedDaysAgo : {5})")
    @CsvSource({
        // ---- Stars increases (forks, recency equal) ----
        "10, 10, 10,   1000, 10, 10",
        "0,  0,  5,    50,   0,  5",

        // ---- Forks increases (stars, recency equal) ----
        "100, 5,  7,   100, 50, 7",
        "20,  0,  3,   20,  10, 3",

        // ---- Recency improves (smaller daysAgo = more recent; stars, forks equal) ----
        "100, 20, 120, 100, 20, 5",
        "5,   1,  60,  5,   1,  1",

        // ---- Mixed improvements (multiple factors better) ----
        "10,  2,  30,  50,  5,  7",
        "200, 1,  14,  300, 2,  3"
    })
    void calculatePopularityScore(int starsA, int forksA, int daysAgoA, int starsB, int forksB, int daysAgoB) {
        var githubRepositoryItem1 = repo("a/repo", starsA, forksA, daysAgoA);
        var githubRepositoryItem2 = repo("b/repo", starsB, forksB, daysAgoB);

        var scoreA = calculator.calculate(githubRepositoryItem1, NOW);
        var scoreB = calculator.calculate(githubRepositoryItem2, NOW);

        assertThat(scoreB)
            .as("Expected B to score higher than A when one or more factors")
            .isGreaterThan(scoreA);
    }
}
