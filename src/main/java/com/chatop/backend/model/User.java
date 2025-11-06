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
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/** Entity representing a user in the system. Maps to the 'users' table in the database. */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

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

  // Returns the authorities granted to the user. Currently returns empty list.
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  // Returns the username used for authentication (email in this case).
  @Override
  public String getUsername() {
    return email;
  }

  // Indicates whether the user's account has expired.
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  // Indicates whether the user is locked or unlocked.
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  // Indicates whether the user's credentials have expired.
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  // Indicates whether the user is enabled or disabled.
  @Override
  public boolean isEnabled() {
    return true;
  }
}
