package com.chatop.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request DTO for creating a rental listing. Bound from multipart/form-data using @ModelAttribute
 * in the controller. All fields are required and validated. The authenticated user is set as the
 * owner.
 */
@Getter
@Setter
@Schema(description = "Multipart form payload for creating a rental")
public class RentalCreateRequest {

  @NotBlank
  @Schema(example = "Studio Loft")
  private String name;

  @NotNull
  @Schema(example = "35.5")
  private BigDecimal surface;

  @NotNull
  @Schema(example = "699.99")
  private BigDecimal price;

  @NotBlank
  @Schema(example = "Close to the city center...")
  private String description;

  @NotNull
  @Schema(description = "Image file for the rental", type = "string", format = "binary")
  private MultipartFile picture;

}
