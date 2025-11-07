package com.chatop.backend.service;

import com.chatop.backend.model.User;
import com.chatop.backend.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service handling user authentication and registration operations.
 */
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

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

  /**
   * Finds a user by their email address.
   *
   * @param email the email to search for
   * @return an Optional containing the user if found, empty otherwise
   */
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  /**
   * Loads user details by email for authentication. Required by Spring Security.
   *
   * @param email the user's email (used as username)
   * @return the authenticated user's details
   * @throws UsernameNotFoundException if no user is found
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByEmail(email)
      .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
  }

  /**
   * Validates a raw password against the stored encrypted password.
   *
   * @param rawPassword     the plain text password to validate
   * @param encodedPassword the stored encrypted password
   * @return true if the passwords match, false otherwise
   */
  public boolean passwordIsValid(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }

}
