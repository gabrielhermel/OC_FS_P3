package com.chatop.backend.service;

import com.chatop.backend.dto.RentalListItemResponse;
import com.chatop.backend.dto.RentalListResponse;
import com.chatop.backend.dto.SingleRentalResponse;
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
    List<RentalListItemResponse> rentalItems =
      rentalRepository.findAll().stream()
        .map(this::toRentalListItem)
        .collect(Collectors.toList());

    return new RentalListResponse(rentalItems);
  }

  /**
   * Retrieves a single rental by its ID and converts it to a DTO.
   *
   * @param id the ID of the rental
   * @return the corresponding SingleRentalResponse DTO, or null if not found
   */
  public SingleRentalResponse getRentalById(Long id) {
    return rentalRepository.findById(id)
      .map(this::toSingleRentalResponse)
      .orElse(null);
  }

  /**
   * Converts a Rental entity into a RentalListItem DTO. Formats timestamps to "yyyy/MM/dd" format
   * for the API response.
   *
   * @param rental the Rental entity to convert
   * @return a DTO representing the rental
   */
  private RentalListItemResponse toRentalListItem(Rental rental) {
    return new RentalListItemResponse(
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

  /**
   * Identical to toRentalListItem but returns SingleRentalResponse DTO which wraps picture in a
   * list to match Mockoon schema.
   *
   * @param rental the Rental entity to convert
   * @return a DTO representing the single rental
   */
  private SingleRentalResponse toSingleRentalResponse(Rental rental) {
    return new SingleRentalResponse(
      rental.getId(),
      rental.getName(),
      rental.getSurface(),
      rental.getPrice(),
      List.of(rental.getPicture()), // Assuming single picture wrapped in a list
      rental.getDescription(),
      rental.getOwner().getId(),
      rental.getCreatedAt() != null ? rental.getCreatedAt().format(DATE_FORMATTER) : null,
      rental.getUpdatedAt() != null ? rental.getUpdatedAt().format(DATE_FORMATTER) : null);
  }

}
