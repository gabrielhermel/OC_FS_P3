package com.chatop.backend.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for standardizing API error responses. Ensures consistency with Mockoon
 * responses (e.g., "{}" for 400 errors).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles validation errors (e.g. @NotNull, @NotBlank violations).
   *
   * @param ex validation exception thrown during request binding
   * @return standardized 400 response with empty JSON body
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
    MethodArgumentNotValidException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of());
  }

  /**
   * Handles invalid JSON structure or missing required properties.
   *
   * @param ex JSON parsing exception
   * @return standardized 400 response with empty JSON body
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, Object>> handleJsonParsingExceptions(
    HttpMessageNotReadableException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of());
  }

  /**
   * Handles IllegalArgumentException (e.g., invalid rental_id or user_id references).
   *
   * @param ex exception indicating invalid entity relationship
   * @return standardized 400 response with empty JSON body
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of());
  }
}
