package com.chatop.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Wrapper DTO for the rentals list response where all rentals are under the "rentals" key.
 *
 * @param rentals list of rental items
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Response containing the list of rentals.")
public record RentalListResponse(
  // For underlying JSON spec
  @ArraySchema(
    arraySchema = @Schema(description = "List of rental objects"),
    schema = @Schema(implementation = RentalListItemResponse.class))
  // For Swagger UI
  @Schema(
    description = "List of rental objects",
    implementation = RentalListItemResponse.class
  )
  List<RentalListItemResponse> rentals
) {

}
