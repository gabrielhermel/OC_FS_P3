package com.chatop.backend.service;

import com.chatop.backend.dto.MessageRequest;
import com.chatop.backend.dto.MessageResponse;
import com.chatop.backend.model.Message;
import com.chatop.backend.model.Rental;
import com.chatop.backend.model.User;
import com.chatop.backend.repository.MessageRepository;
import com.chatop.backend.repository.RentalRepository;
import com.chatop.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service handling message-related operations. Responsible for saving messages sent by
 * authenticated users about specific rentals.
 */
@Service
@RequiredArgsConstructor
public class MessageService {

  private final MessageRepository messageRepository;
  private final RentalRepository rentalRepository;
  private final UserRepository userRepository;

  /**
   * Creates and saves a new message.
   *
   * @param request the incoming message data (rental ID and content)
   * @param userId  the authenticated user's ID
   * @return confirmation response after saving
   * @throws IllegalArgumentException if the user or rental does not exist
   */
  public MessageResponse sendMessage(MessageRequest request, Long userId) {
    // Fetch user and rental entities to maintain referential integrity
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    Rental rental = rentalRepository.findById(request.rentalId())
      .orElseThrow(
        () -> new IllegalArgumentException("Rental not found with ID: " + request.rentalId()));

    // Create and save the message entity
    Message message = new Message();
    message.setUser(user);
    message.setRental(rental);
    message.setMessage(request.message());

    messageRepository.save(message);

    return new MessageResponse("Message sent with success");
  }

}
