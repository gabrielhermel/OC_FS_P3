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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a rental listing. Maps to the 'rentals' table and links to the owning User.
 */
@Entity
@Table(name = "rentals")
@Getter
@Setter
@NoArgsConstructor
public class Rental {

  // Unique identifier for the message. Auto-generated.
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // The name of the rental listing.
  @Column(name = "name", length = 255)
  private String name;

  // The surface area of the rental in square meters.
  @Column(name = "surface")
  private BigDecimal surface;

  // The price of the rental.
  @Column(name = "price")
  private BigDecimal price;

  // URL or path to the rental picture.
  @Column(name = "picture", length = 255)
  private String picture;

  // Description of the rental.
  @Column(name = "description", length = 2000)
  private String description;

  // Owner of the rental (FK: rentals.owner_id -> users.id).
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id", nullable = false)
  private User owner;

  // Timestamp when the rental was created.
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  // Timestamp when the rental was last updated.
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  // Set timestamps before persisting new rental.
  @PrePersist
  public void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  // Update the timestamp before updating an existing rental.
  @PreUpdate
  public void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

}
