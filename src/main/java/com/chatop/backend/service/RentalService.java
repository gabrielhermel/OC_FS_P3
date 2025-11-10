package com.chatop.backend.service;

import com.chatop.backend.dto.RentalCreateRequest;
import com.chatop.backend.dto.RentalListItemResponse;
import com.chatop.backend.dto.RentalListResponse;
import com.chatop.backend.dto.SingleRentalResponse;
import com.chatop.backend.dto.StatusMessageResponse;
import com.chatop.backend.model.Rental;
import com.chatop.backend.model.User;
import com.chatop.backend.repository.RentalRepository;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
   * Image storage service used to validate and save uploaded rental pictures
   */
  private final RentalImageStorageService rentalImageStorageService;

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

  /**
   * Creates a new rental listing for the specified owner. Persists a new Rental entity without a
   * picture URL to obtain the generated ID, saves the image using RentalImageStorageService,
   * updates the Rental with the returned URL, and commits the transaction.
   *
   * @param request multipart form containing name, surface, price, description, and picture
   * @param owner   the authenticated user creating the rental
   * @return StatusMessageResponse with "Rental created!"
   */
  @Transactional // Ensures if the initial insert or the update fails, no rental is persisted
  public StatusMessageResponse createRental(RentalCreateRequest request, User owner) {
    // Create Rental entity without picture
    Rental rental = new Rental();
    rental.setName(request.getName());
    rental.setSurface(request.getSurface());
    rental.setPrice(request.getPrice());
    rental.setDescription(request.getDescription());
    rental.setOwner(owner);
    // Persist to get generated ID
    rental = rentalRepository.save(rental);

    // Save image and get URL
    String pictureUrl =
      rentalImageStorageService.saveRentalImage(request.getPicture(), rental.getId());

    // Update rental with picture URL
    rental.setPicture(pictureUrl);
    // Save updated rental
    rentalRepository.save(rental);

    // Return status message
    return new StatusMessageResponse("Rental created!");
  }

}
