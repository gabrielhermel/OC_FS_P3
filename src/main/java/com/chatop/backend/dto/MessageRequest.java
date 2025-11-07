package com.chatop.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for sending a message. Carries the message content and target rental ID. The
 * authenticated user is inferred from the JWT token.
 *
 * @param rentalId ID of the rental the message refers to (required)
 * @param message  message content (required)
 */
@Schema(description = "Request payload for sending a message")
public record MessageRequest(
  @JsonProperty("rental_id") @Schema(example = "1") @NotNull Long rentalId,
  @Schema(example = "This is a message about a rental...") @NotBlank String message
) {

}
