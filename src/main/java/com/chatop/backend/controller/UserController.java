package com.chatop.backend.controller;

import com.chatop.backend.annotation.GetByIdErrorResponses;
import com.chatop.backend.dto.UserResponse;
import com.chatop.backend.model.User;
import com.chatop.backend.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for retrieving user information. Provides endpoint for fetching public user data
 * by ID.
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(
  name = "Users",
  description = "Endpoints for retrieving user information"
)
public class UserController {

  private final UserRepository userRepository;

  /**
   * Retrieves a user's profile by ID.
   *
   * @param id                the user ID to look up
   * @param authenticatedUser the currently authenticated user (inferred from JWT)
   * @return the user's information or an empty body if not found
   */
  @Operation(
    summary = "Get user by ID",
    description = "Returns information for a user identified by ID.",
    security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponse(
    responseCode = "200",
    description = "User details retrieved successfully",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = UserResponse.class)
    )
  )
  @GetByIdErrorResponses
  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(
    @PathVariable Long id,
    @AuthenticationPrincipal User authenticatedUser
  ) {
    // If authentication fails
    if (authenticatedUser == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new UserResponse(null, null, null, null, null));
    }

    // Fetch user by ID
    Optional<User> userOpt = userRepository.findById(id);
    if (userOpt.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new UserResponse(null, null, null, null, null));
    }

    User user = userOpt.get();
    // Construct response DTO
    UserResponse response = new UserResponse(
      user.getId(),
      user.getName(),
      user.getEmail(),
      user.getCreatedAt(),
      user.getUpdatedAt()
    );

    return ResponseEntity.ok(response);

  }

}
