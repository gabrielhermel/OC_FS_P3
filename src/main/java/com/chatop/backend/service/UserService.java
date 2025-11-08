package com.chatop.backend.service;

import com.chatop.backend.dto.UserResponse;
import com.chatop.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service handling user-related operations such as retrieval by ID.
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  /**
   * Retrieves a user by their ID and converts it into a DTO.
   *
   * @param id the user’s ID
   * @return UserResponse DTO, or null if not found
   */
  public UserResponse getUserById(Long id) {
    return userRepository.findById(id)
      .map(user -> new UserResponse(
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getCreatedAt(),
        user.getUpdatedAt()
      ))
      .orElse(null);
  }

}
