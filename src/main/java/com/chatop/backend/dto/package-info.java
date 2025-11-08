/**
 * Data Transfer Objects (DTOs) for input and output between the API and clients.
 * <p>
 * DTOs ensure that API payloads remain independent from database entities, allowing internal models
 * to evolve without breaking external contracts.
 * <p>
 * Examples:
 * <ul>
 *   <li>{@link com.chatop.backend.dto.UserResponse} — response model for authenticated users</li>
 *   <li>{@link com.chatop.backend.dto.MessageRequest} — input model for sending messages</li>
 *   <li>{@link com.chatop.backend.dto.RentalListResponse} — wrapper for listing rentals</li>
 * </ul>
 */
package com.chatop.backend.dto;
