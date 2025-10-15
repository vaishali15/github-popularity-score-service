package com.challenge.github.popularityscore.service;

import com.challenge.github.popularityscore.config.ScoringProperties;
import com.challenge.github.popularityscore.dto.ingress.GithubRepositoryItemDto;
import com.challenge.github.popularityscore.model.PopularityScore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;

/**
 * Calculates scores from stars, forks, and update recency.
 */
@Service
@RequiredArgsConstructor
public class PopularityScoreCalculator {

    private final ScoringProperties scoring;

    public PopularityScore calculate(GithubRepositoryItemDto githubRepositoryItemDto, OffsetDateTime now) {
        double stars = Math.log1p(githubRepositoryItemDto.stargazersCount()) * scoring.starsWeight();
        double forks = Math.log1p(githubRepositoryItemDto.forksCount()) * scoring.forksWeight();

        long days = Math.max(0, Duration.between(githubRepositoryItemDto.updatedAt(), now).toDays());
        double lambda = Math.log(2.0) / scoring.recencyHalfLifeDays();
        double recency = Math.exp(-lambda * days) * scoring.recencyWeight();

        return new PopularityScore(stars + forks + recency);
    }
}
