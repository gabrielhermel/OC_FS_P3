package com.chatop.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO returned after successfully sending a message.
 *
 * @param message confirmation message
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Response containing the message sending confirmation")
public record MessageResponse(
  @Schema(example = "Message sent with success") String message
) {

}
