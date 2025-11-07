package com.chatop.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for user login.
 *
 * @param email    user's email address (required, must be valid email format)
 * @param password user's password (required)
 */
@Schema(description = "Request payload for user login")
public record LoginRequest(
  @Schema(example = "john.doe@example.com") @NotBlank @Email String email,
  @Schema(example = "password123") @NotBlank String password) {

}
