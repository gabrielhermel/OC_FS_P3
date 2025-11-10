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
  @Schema(example = "Studio Loft") String name,
  @Schema(example = "35.5") BigDecimal surface,
  @Schema(example = "699.99") BigDecimal price,
  // For underlying JSON spec
  @ArraySchema(
    arraySchema = @Schema(description = "List of image resource URLs"),
    schema = @Schema(example = "/rental_images/rental_1_1731254789123.jpg"))
  // For Swagger UI
  @Schema(
    description = "List of image resource URLs",
    example = "[\"/rental_images/rental_1_1731254789123.jpg\"]"
  )
  List<String> picture,
  @Schema(example = "Close to the city center...") String description,
  @JsonProperty("owner_id") @Schema(example = "1") Long ownerId,
  @JsonProperty("created_at") @Schema(example = "2025/10/6") String createdAt,
  @JsonProperty("updated_at") @Schema(example = "2025/10/7") String updatedAt
) {

}
