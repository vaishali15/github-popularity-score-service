package com.challenge.github.popularityscore.service;

import com.challenge.github.popularityscore.config.ScoreConfig;
import com.challenge.github.popularityscore.external.github.model.GithubRepositoryItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.OffsetDateTime;

/**
 * Calculates scores from stars, forks, and update recency.
 */
@Service
@RequiredArgsConstructor
public class PopularityScoreCalculator {

    private final ScoreConfig scoring;

    public double calculate(GithubRepositoryItem githubRepositoryItem, OffsetDateTime now) {
        double stars = Math.log1p(githubRepositoryItem.stargazersCount()) * scoring.starsWeight();
        double forks = Math.log1p(githubRepositoryItem.forksCount()) * scoring.forksWeight();

        long days = Math.max(0, Duration.between(githubRepositoryItem.updatedAt(), now).toDays());
        double lambda = Math.log(2.0) / scoring.recencyHalfLifeDays();
        double recency = Math.exp(-lambda * days) * scoring.recencyWeight();

        var score = stars + forks + recency;
        var df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(score));
    }
}
