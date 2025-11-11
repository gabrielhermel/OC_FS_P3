package com.chatop.backend.exception;

/**
 * Thrown when a requested resource (rental, user, etc.) is not found in the database.
 */
public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message) {
    super(message);
  }

}
