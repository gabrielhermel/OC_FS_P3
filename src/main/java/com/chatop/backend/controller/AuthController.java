package com.chatop.backend.controller;

import com.chatop.backend.model.User;
import com.chatop.backend.service.AuthService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for authentication endpoints. Temporary implementation for testing.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  /**
   * Registers a new user.
   *
   * @param body request body containing email, name, and password
   * @return ResponseEntity with the created User
   */
  @PostMapping("/register")
  public ResponseEntity<User> register(@RequestBody Map<String, String> body) {
    String email = body.get("email");
    String name = body.get("name");
    String password = body.get("password");

    User user = authService.registerUser(email, name, password);
    return ResponseEntity.ok(user);
  }
}
