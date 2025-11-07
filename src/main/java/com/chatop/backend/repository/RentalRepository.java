package com.chatop.backend.repository;

import com.chatop.backend.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Rental persistence operations. Provides CRUD methods via Spring Data JPA.
 */
@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

}
