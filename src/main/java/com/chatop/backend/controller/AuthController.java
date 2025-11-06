package com.chatop.backend.controller;

import com.chatop.backend.dto.LoginRequest;
import com.chatop.backend.dto.LoginResponse;
import com.chatop.backend.dto.RegisterRequest;
import com.chatop.backend.dto.UserResponse;
import com.chatop.backend.model.User;
import com.chatop.backend.security.JwtService;
import com.chatop.backend.service.AuthService;
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
 * REST controller for authentication endpoints.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final JwtService jwtService;

  /**
   * Registers a new user.
   *
   * @param request the registration request containing email, name, and password
   * @return ResponseEntity with the created User
   */
  @PostMapping("/register")
  public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
    User user = authService.registerUser(request.email(), request.name(), request.password());
    return ResponseEntity.ok(user);
  }

  /**
   * Authenticates a user and returns a JWT token.
   *
   * @param request the login request containing email and password
   * @return ResponseEntity with LoginResponse containing the JWT token
   * @throws RuntimeException if credentials are invalid
   */
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
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
  }

  /**
   * Returns the information of the currently authenticated user.
   *
   * @param user the authenticated user entity provided by Spring Security
   * @return the authenticated user's details
   */
  @GetMapping("/me")
  public ResponseEntity<UserResponse> getAuthenticatedUser(@AuthenticationPrincipal User user) {
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    UserResponse response = new UserResponse(
      user.getId().intValue(),
      user.getName(),
      user.getEmail(),
      user.getCreatedAt(),
      user.getUpdatedAt()
    );

    return ResponseEntity.ok(response);
  }

}
