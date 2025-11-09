package com.chatop.backend.exception;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * Centralized exception handler for all REST controllers. Returns consistent empty JSON bodies for
 * error responses and logs at appropriate levels for monitoring and debugging.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @Value("${app.upload.max-size}")
  private String maxUploadSizeConfigured;

  /**
   * Handles validation errors from @Valid/@Validated annotations. Triggered by constraint
   * violations like @NotNull, @NotBlank, @Email.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
    MethodArgumentNotValidException ex) {
    log.debug("Validation failed: {}", ex.getBindingResult().getAllErrors());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of());
  }

  /**
   * Handles malformed JSON or invalid request body structure.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, Object>> handleJsonParsingExceptions(
    HttpMessageNotReadableException ex) {
    log.debug("JSON parsing failed: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of());
  }

  /**
   * Handles business logic validation failures from service layer. Includes invalid IDs,
   * unsupported file types, or corrupted images.
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
    log.warn("Invalid request: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of());
  }

  /**
   * Handles internal server errors from service layer. Includes file system failures, database
   * issues, or unexpected runtime errors.
   */
  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
    log.error("Internal server error: {}", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of());
  }

  /**
   * Handles missing required multipart request parameters.
   */
  @ExceptionHandler(MissingServletRequestPartException.class)
  public ResponseEntity<Map<String, Object>> handleMissingPart(
    MissingServletRequestPartException ex) {
    log.debug("Missing multipart request part: {}", ex.getRequestPartName());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of());
  }

  /**
   * Handles file upload size limit violations.
   */
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<Map<String, Object>> handleMaxUpload(MaxUploadSizeExceededException ex) {
    log.warn("Upload size exceeded (max: {})",
      ex.getMaxUploadSize() == -1 ? maxUploadSizeConfigured : ex.getMaxUploadSize() + " bytes");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of());
  }
}
