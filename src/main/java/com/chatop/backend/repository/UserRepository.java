package com.chatop.backend.repository;

import com.chatop.backend.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository interface for User entity database operations. */
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Finds a user by their email address.
   *
   * @param email the email to search for
   * @return an Optional containing the user if found, empty otherwise
   */
  Optional<User> findByEmail(String email);

  /**
   * Checks if a user exists with the given email.
   *
   * @param email the email to check
   * @return true if a user with this email exists, false otherwise
   */
  boolean existsByEmail(String email);
}
