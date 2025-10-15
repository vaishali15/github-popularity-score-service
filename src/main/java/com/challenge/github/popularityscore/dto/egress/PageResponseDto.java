package com.challenge.github.popularityscore.dto.egress;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(
    name = "Page",
    description = "Paging metadata and page content. The item type (T) is inferred from the endpoint."
)
public record PageResponseDto<T>(
    @Schema(description = "Requested page index (1-based).", example = "1")
    int page,

    @Schema(description = "Requested page size.", example = "10")
    int size,

    @Schema(description = "Number of items in this page.", example = "10")
    int total,

    @Schema(description = "Total items available on GitHub.", example = "123456")
    Integer totalAvailable,

    @ArraySchema(arraySchema = @Schema(description = "Page content items."))
    List<T> items) {
}
