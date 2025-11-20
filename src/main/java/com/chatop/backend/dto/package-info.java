/**
 * Data Transfer Objects (DTOs) for input and output between the API and clients.
 * <p>
 * DTOs prevent exposing internal JPA entities directly to the outside world.
 * They keep contracts with API consumers stable, even if persistence models change,
 * and allow incoming and outgoing data to be shaped for specific use cases.
 * <p>
 * Examples:
 * <ul>
 *   <li>{@link com.chatop.backend.dto.UserResponse} — response model for authenticated users</li>
 *   <li>{@link com.chatop.backend.dto.MessageRequest} — input model for sending messages</li>
 *   <li>{@link com.chatop.backend.dto.RentalListResponse} — wrapper for listing rentals</li>
 * </ul>
 */
package com.chatop.backend.dto;
