package com.chatop.backend.service;

import com.chatop.backend.dto.RentalCreateRequest;
import com.chatop.backend.dto.RentalListItemResponse;
import com.chatop.backend.dto.RentalListResponse;
import com.chatop.backend.dto.RentalUpdateRequest;
import com.chatop.backend.dto.SingleRentalResponse;
import com.chatop.backend.dto.StatusMessageResponse;
import com.chatop.backend.exception.ResourceNotFoundException;
import com.chatop.backend.model.Rental;
import com.chatop.backend.model.User;
import com.chatop.backend.repository.RentalRepository;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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

  /**
   * Updates an existing rental. Only the owner can perform the update. If a new picture is
   * uploaded, it replaces the old one in the database, but the old image file remains on disk. At
   * least one field must be provided; otherwise, a 400 Bad Request is thrown.
   *
   * @param rentalId ID of rental to update
   * @param request  multipart form data with updated fields
   * @param user     authenticated user from security context
   * @return StatusMessageResponse with "Rental updated!"
   */
  @Transactional // Prevents partial updates if any step fails
  public StatusMessageResponse updateRental(Long rentalId, RentalUpdateRequest request, User user) {
    Rental rental = rentalRepository.findById(rentalId)
      .orElseThrow(() -> new ResourceNotFoundException("Rental not found with ID: " + rentalId));

    Long userId = user.getId();
    boolean updated = false; // Track if any field was actually modified

    // Verify ownership
    if (!rental.getOwner().getId().equals(userId)) {
      throw new AccessDeniedException(
        "User (ID: " + userId + ") is not the owner of rental (ID: " + rentalId + ").");
    }

    // Update fields if provided
    if (request.getName() != null && !request.getName().isBlank()) {
      rental.setName(request.getName());
      updated = true;
    }
    if (request.getSurface() != null) {
      rental.setSurface(request.getSurface());
      updated = true;
    }
    if (request.getPrice() != null) {
      rental.setPrice(request.getPrice());
      updated = true;
    }
    if (request.getDescription() != null && !request.getDescription().isBlank()) {
      rental.setDescription(request.getDescription());
      updated = true;
    }
    if (request.getPicture() != null && !request.getPicture().isEmpty()) {
      String pictureUrl =
        rentalImageStorageService.saveRentalImage(request.getPicture(), rental.getId());
      rental.setPicture(pictureUrl);
      updated = true;
    }

    // If no fields were updated, throw exception
    if (!updated) {
      throw new IllegalArgumentException("At least one field must be provided for update.");
    }

    rentalRepository.save(rental);

    return new StatusMessageResponse("Rental updated!");
  }
}
