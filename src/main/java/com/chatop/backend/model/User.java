package com.chatop.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Entity representing a user in the system. Maps to the 'users' table in the database.
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

  // Unique identifier for the user. Auto-generated.
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // User's email address. Must be unique and non-null.
  @Column(nullable = false, unique = true)
  private String email;

  // User's display name.
  @Column(nullable = false)
  private String name;

  // User's encrypted password.
  @Column(nullable = false)
  private String password;

  // Timestamp when the user was created.
  private LocalDateTime createdAt;

  //Timestamp when the user was last updated.
  private LocalDateTime updatedAt;

  //Sets creation and update timestamps before persisting a new user.
  @PrePersist
  public void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  //Updates the timestamp before updating an existing user.
  @PreUpdate
  public void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
