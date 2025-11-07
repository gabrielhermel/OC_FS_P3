package com.chatop.backend.repository;

import com.chatop.backend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Message persistence operations. Provides CRUD methods via Spring Data JPA.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
