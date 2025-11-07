package com.chatop.backend.service;

import com.chatop.backend.dto.RentalListItem;
import com.chatop.backend.dto.RentalListResponse;
import com.chatop.backend.model.Rental;
import com.chatop.backend.repository.RentalRepository;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service handling rental-related operations. Responsible for retrieving, creating, and updating
 * rental data.
 */
@Service
@RequiredArgsConstructor
public class RentalService {

  /**
   * Date formatter for converting timestamps to "yyyy/MM/dd" format in API responses.
   */
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");
  private final RentalRepository rentalRepository;

  /**
   * Retrieves all rentals from the database and converts them into DTOs.
   *
   * @return a response containing the list of all rentals
   */
  public RentalListResponse getAllRentals() {
    List<RentalListItem> rentalItems =
      rentalRepository.findAll().stream()
        .map(this::toRentalListItem)
        .collect(Collectors.toList());

    return new RentalListResponse(rentalItems);
  }

  /**
   * Converts a Rental entity into a RentalListItem DTO. Formats timestamps to "yyyy/MM/dd" format
   * for the API response.
   *
   * @param rental the Rental entity to convert
   * @return a DTO representing the rental
   */
  private RentalListItem toRentalListItem(Rental rental) {
    return new RentalListItem(
      rental.getId(),
      rental.getName(),
      rental.getSurface(),
      rental.getPrice(),
      rental.getPicture(),
      rental.getDescription(),
      rental.getOwner().getId(),
      rental.getCreatedAt() != null ? rental.getCreatedAt().format(DATE_FORMATTER) : null,
      rental.getUpdatedAt() != null ? rental.getUpdatedAt().format(DATE_FORMATTER) : null);
  }
}
