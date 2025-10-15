package com.challenge.github.popularityscore.dto.egress;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Generic paginated response wrapper.
 *
 * @param <T> item type
 */
@Jacksonized
@Builder
public record PageResponseDto<T>(int page,
                                 int size,
                                 int total,
                                 Integer totalAvailable,
                                 List<T> items) {
}
