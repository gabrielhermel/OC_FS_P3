package com.chatop.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for user registration.
 *
 * @param email    user's email address (required, must be valid email format)
 * @param name     user's display name (required)
 * @param password user's password (required)
 */
public record RegisterRequest(
  @NotBlank @Email String email,
  @NotBlank String name,
  @NotBlank String password) {

}
