package com.chatop.backend.dto;

import java.time.LocalDateTime;

/**
 * Response DTO representing authenticated user information.
 *
 * @param id        unique identifier of the user
 * @param name      user's display name
 * @param email     user's email address
 * @param createdAt timestamp when the user was created
 * @param updatedAt timestamp when the user was last updated
 */
public record UserResponse(
  int id,
  String name,
  String email,
  LocalDateTime createdAt,
  LocalDateTime updatedAt
) {

}
