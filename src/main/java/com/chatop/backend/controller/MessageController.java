package com.chatop.backend.controller;

import com.chatop.backend.annotation.PostSecuredErrorResponses;
import com.chatop.backend.dto.MessageRequest;
import com.chatop.backend.dto.MessageResponse;
import com.chatop.backend.model.User;
import com.chatop.backend.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling message operations. Allows authenticated users to send messages
 * related to rentals.
 */
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(
  name = "Messages",
  description = "Endpoints for sending messages about rentals"
)
public class MessageController {

  private final MessageService messageService;

  /**
   * Creates a new message associated with a rental. The authenticated user is automatically set as
   * the sender.
   *
   * @param request the message details (validated DTO)
   * @param user    the authenticated user (inferred from JWT)
   * @return confirmation message
   */
  @Operation(
    summary = "Create a message linked to a rental",
    description = "Stores a new message in the database, "
      + "linked to the specified rental and the authenticated user.",
    security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponse(
    responseCode = "200",
    description = "Message sent successfully",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = MessageResponse.class)
    ))
  @PostSecuredErrorResponses
  @PostMapping
  public ResponseEntity<MessageResponse> sendMessage(
    @Valid @RequestBody MessageRequest request,
    @AuthenticationPrincipal User user
  ) {

    // Additional safety check, though Spring Security should handle this
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    try {
      // Delegate message processing to service layer for business logic
      MessageResponse response = messageService.sendMessage(request, user.getId());
      // Return 200 OK with the created message response
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      // Return 400 Bad Request for any validation errors
      return ResponseEntity.badRequest().body(new MessageResponse(null));
    }
  }

}
