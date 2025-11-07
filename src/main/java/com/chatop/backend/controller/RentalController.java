package com.chatop.backend.controller;

import com.chatop.backend.dto.RentalListResponse;
import com.chatop.backend.model.User;
import com.chatop.backend.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for rental-related endpoints. Handles retrieving and managing rental listings.
 */
@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
@Tag(
  name = "Rentals",
  description = "Endpoints for retrieving and managing rental listings"
)
public class RentalController {

  private final RentalService rentalService;

  /**
   * Retrieves all available rentals. Requires a valid JWT token.
   *
   * @param user the authenticated user entity provided by Spring Security
   * @return list of all rentals wrapped in a response DTO
   */
  @Operation(
    summary = "Get all rentals",
    description = "Returns the list of all rentals. Requires authentication.",
    security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "List of rentals retrieved successfully",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = RentalListResponse.class)
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Unauthorized or missing JWT",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(example = "{}")
      )
    ),
    @ApiResponse(
      responseCode = "403",
      description = "Forbidden: insufficient permissions",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(example = "{}")
      )
    )
  })
  @GetMapping
  public ResponseEntity<RentalListResponse> getAllRentals(
    @Parameter(hidden = true) @AuthenticationPrincipal User user
  ) {
    // Explicit null check safety measure (Spring Security handles authentication)
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    RentalListResponse response = rentalService.getAllRentals();
    return ResponseEntity.ok(response);
  }

}
