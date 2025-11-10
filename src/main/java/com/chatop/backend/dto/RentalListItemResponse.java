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
public record RentalListItemResponse(
  @Schema(example = "1") Long id,
  @Schema(example = "Studio Loft") String name,
  @Schema(example = "35.5") BigDecimal surface,
  @Schema(example = "699.99") BigDecimal price,
  @Schema(example = "/rental_images/rental_1_1731254789123.jpg") String picture,
  @Schema(example = "Close to the city center...") String description,
  @JsonProperty("owner_id") @Schema(example = "1") Long ownerId,
  @JsonProperty("created_at") @Schema(example = "2025/10/6") String createdAt,
  @JsonProperty("updated_at") @Schema(example = "2025/10/7") String updatedAt
) {

}
