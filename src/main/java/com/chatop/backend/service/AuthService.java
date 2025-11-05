package com.chatop.backend.service;

import com.chatop.backend.model.User;
import com.chatop.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Service handling user authentication and registration operations.
@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * Registers a new user with encrypted password.
   *
   * @param email       the user's email address
   * @param name        the user's display name
   * @param rawPassword the user's plain text password
   * @return the created User entity
   * @throws IllegalArgumentException if email is already in use
   */
  public User registerUser(String email, String name, String rawPassword) {
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("Email already in use");
    }

    User user = new User();
    user.setEmail(email);
    user.setName(name);
    user.setPassword(passwordEncoder.encode(rawPassword));

    return userRepository.save(user);
  }
}
