package com.chatop.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Response containing authenticated user details")
public record UserResponse(
  @Schema(example = "1") Integer id,
  @Schema(example = "John Doe") String name,
  @Schema(example = "john.doe@example.com") String email,
  @Schema(example = "2025-11-06T12:00:00") LocalDateTime createdAt,
  @Schema(example = "2025-11-06T12:00:00") LocalDateTime updatedAt
) {

}
