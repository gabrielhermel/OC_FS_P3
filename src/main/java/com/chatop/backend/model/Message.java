package com.chatop.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a message sent by a user about a rental.
 * Maps to the 'messages' table with foreign keys to 'users' and 'rentals'.
 */
@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
public class Message {

  // Unique identifier for the message. Auto-generated.
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Associated rental (FK: messages.rental_id -> rentals.id).
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "rental_id", nullable = false)
  private Rental rental;

  // Associated user who sent the message (FK: messages.user_id -> users.id).
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  // Content of the message (required).
  @Column(name = "message", nullable = false, length = 2000)
  private String message;

  // Timestamp when the message was created.
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  // Timestamp when the message was last updated.
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  // Set timestamps before persisting new message.
  @PrePersist
  public void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  // Update timestamp before updating existing message.
  @PreUpdate
  public void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

}
