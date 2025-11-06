package com.chatop.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for user login.
 *
 * @param email    user's email address (required, must be valid email format)
 * @param password user's password (required)
 */
public record LoginRequest(
  @NotBlank @Email String email,
  @NotBlank String password) {

}
