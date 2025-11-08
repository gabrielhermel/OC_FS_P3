/**
 * REST controllers that define the application's HTTP endpoints.
 * <p>
 * Controllers are responsible for handling HTTP requests, performing validation, and returning
 * responses (usually via DTOs). They delegate all business logic to the service layer.
 * <p>
 * Each controller is annotated with {@link org.springframework.web.bind.annotation.RestController}
 * and typically has route mappings starting with {@code /api/}.
 * <p>
 * Example: {@link com.chatop.backend.controller.AuthController},
 * {@link com.chatop.backend.controller.MessageController}.
 */
package com.chatop.backend.controller;
