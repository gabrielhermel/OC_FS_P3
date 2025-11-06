package com.chatop.backend.dto;

/**
 * Response DTO for successful login.
 *
 * @param token JWT authentication token
 */
public record LoginResponse(String token) {

}
