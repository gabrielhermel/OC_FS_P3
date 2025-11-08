package com.chatop.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

/**
 * Represents a single rental in the detailed rental endpoint.
 *
 * @param id          rental ID
 * @param name        rental name
 * @param surface     surface area
 * @param price       price
 * @param picture     picture URLs (single-item array at present)
 * @param description description text
 * @param ownerId     owner user ID
 * @param createdAt   creation date as string
 * @param updatedAt   update date as string
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Response for a single rental resource.")
public record SingleRentalResponse(
  @Schema(example = "1") Long id,
  @Schema(example = "House 1") String name,
  @Schema(example = "100.5") BigDecimal surface,
  @Schema(example = "750.5") BigDecimal price,
  @ArraySchema(
    arraySchema = @Schema(description = "List of picture URLs"),
    schema = @Schema(example = "https://example.com/pic.jpg"))
  List<String> picture,
  @Schema(example = "Lorem ipsum dolor sit amet...") String description,
  @JsonProperty("owner_id") @Schema(example = "1") Long ownerId,
  @JsonProperty("created_at") @Schema(example = "2025/10/6") String createdAt,
  @JsonProperty("updated_at") @Schema(example = "2025/10/7") String updatedAt
) {

}
