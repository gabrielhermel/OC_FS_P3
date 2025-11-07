package com.chatop.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for user registration.
 *
 * @param email    user's email address (required, must be valid email format)
 * @param name     user's display name (required)
 * @param password user's password (required)
 */
@Schema(description = "Request payload for user registration")
public record RegisterRequest(
  @Schema(example = "john.doe@example.com") @NotBlank @Email String email,
  @Schema(example = "John Doe") @NotBlank String name,
  @Schema(example = "password123") @NotBlank String password
) {

}
