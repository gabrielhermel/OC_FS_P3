package com.chatop.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO for successful login.
 *
 * @param token JWT authentication token
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Response containing the generated JWT token")
public record LoginResponse(
  @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
  String token
) {

}
