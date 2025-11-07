package com.chatop.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * One rental item in the rentals list.
 *
 * @param id          rental ID
 * @param name        rental name
 * @param surface     surface area
 * @param price       price
 * @param picture     picture URL
 * @param description description text
 * @param ownerId     owner user ID
 * @param createdAt   creation date as string
 * @param updatedAt   update date as string
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "One rental item returned by the rentals list endpoint.")
public record RentalListItem(
  @Schema(example = "1") Long id,
  @Schema(example = "House 1") String name,
  @Schema(example = "100.5") BigDecimal surface,
  @Schema(example = "750.5") BigDecimal price,
  @Schema(example = "https://example.com/pic.jpg") String picture,
  @Schema(example = "Lorem ipsum dolor sit amet...") String description,
  @JsonProperty("owner_id") @Schema(example = "1") Long ownerId,
  @JsonProperty("created_at") @Schema(example = "2025/10/6") String createdAt,
  @JsonProperty("updated_at") @Schema(example = "2025/10/7") String updatedAt
) {

}
