package com.chatop.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request DTO for updating a rental listing. Bound from multipart/form-data using @ModelAttribute
 * in the controller. All fields are optional; only provided fields are updated. At least one field
 * must be provided. Only the owner can update their rental.
 */
@Getter
@Setter
@Schema(description = "Multipart form payload for updating a rental")
public class RentalUpdateRequest {

  @Schema(example = "Studio Loft")
  private String name;

  @Schema(example = "35.5")
  private BigDecimal surface;

  @Schema(example = "699.99")
  private BigDecimal price;

  @Schema(example = "Close to the city center...")
  private String description;

  @Schema(description = "New image file for the rental", type = "string", format = "binary")
  private MultipartFile picture;

}
