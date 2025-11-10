package com.chatop.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Generic response DTO containing a status or confirmation message. Returned after actions such as
 * successfully sending a rental message or creating/updating a rental.
 *
 * @param message status or confirmation message
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Generic response containing a status or confirmation message")
public record StatusMessageResponse(
  @Schema(example = "Rental created!") String message
) {

}
