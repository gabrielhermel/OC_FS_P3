package com.chatop.backend.controller;

import com.chatop.backend.annotation.GetByIdErrorResponses;
import com.chatop.backend.annotation.PostUnsecuredErrorResponses;
import com.chatop.backend.dto.LoginRequest;
import com.chatop.backend.dto.LoginResponse;
import com.chatop.backend.dto.RegisterRequest;
import com.chatop.backend.dto.UserResponse;
import com.chatop.backend.model.User;
import com.chatop.backend.security.JwtService;
import com.chatop.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for authentication endpoints. Handles user registration, login, and JWT-protected
 * user information retrieval.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
  name = "Authentication",
  description = "Endpoints for user registration, login, and JWT-protected user retrieval"
)
public class AuthController {

  private final AuthService authService;
  private final JwtService jwtService;

  /**
   * Registers a new user. Note: Passwords are encoded before storage. Email uniqueness is
   * enforced.
   *
   * @param request the registration request containing email, name, and password
   * @return ResponseEntity containing the registered user's public information
   */
  @Operation(
    summary = "Register a new user",
    description = "Creates a new user account. Accessible without authentication."
  )
  @ApiResponse(
    responseCode = "200",
    description = "User registered successfully",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = UserResponse.class)
    ))
  @PostUnsecuredErrorResponses
  @PostMapping("/register")
  public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
    try {
      // Delegate to service layer for business logic
      User user = authService.registerUser(request.email(), request.name(), request.password());

      // Convert entity to DTO
      UserResponse response = new UserResponse(
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getCreatedAt(),
        user.getUpdatedAt()
      );

      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      // Email already in use or other validation error
      return ResponseEntity.badRequest().body(new UserResponse(null, null, null, null, null));
    }

  }

  /**
   * Authenticates a user and returns a JWT token.
   *
   * @param request the login request containing email and password
   * @return ResponseEntity with LoginResponse containing the JWT token
   * @throws RuntimeException if credentials are invalid
   */
  @Operation(
    summary = "Authenticate a user",
    description = "Authenticates user credentials and returns a JWT token. "
      + "Accessible without authentication."
  )
  @ApiResponse(
    responseCode = "200",
    description = "User authenticated successfully",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = LoginResponse.class)
    ))
  @PostUnsecuredErrorResponses
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    try {
      // Find user by email
      User user = authService.findByEmail(request.email()).orElseThrow(() ->
        new RuntimeException("Invalid credentials"));

      // Verify password using the encoder
      if (!authService.passwordIsValid(request.password(), user.getPassword())) {
        throw new RuntimeException("Invalid credentials");
      }

      // Generate JWT token
      String token = jwtService.generateToken(Map.of(), user.getEmail());

      // Return the token in a DTO
      return ResponseEntity.ok(new LoginResponse(token));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(null));
    }
  }

  /**
   * Returns the information of the currently authenticated user.
   *
   * @param user the authenticated user entity provided by Spring Security
   * @return the authenticated user's details
   */
  @Operation(
    summary = "Get authenticated user info",
    description = "Returns details of the currently authenticated user. Requires a valid JWT token.",
    security = {@SecurityRequirement(name = "bearerAuth")}
  )
  @ApiResponse(
    responseCode = "200",
    description = "User details retrieved successfully",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = UserResponse.class)
    ))
  @GetByIdErrorResponses
  @GetMapping("/me")
  public ResponseEntity<UserResponse> getAuthenticatedUser(
    @Parameter(hidden = true) @AuthenticationPrincipal User user) {
    // Additional safety check, though Spring Security should handle this
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new UserResponse(null, null, null, null, null));
    }

    // Convert entity to DTO
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
