package com.chatop.backend.service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles upload, validation, and storage of rental property images. Validates images by size, MIME
 * type (using Tika), and integrity (using ImageIO). Stores files locally with generated filenames:
 * rental_{rental_id}_{timestamp}_{extension}
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RentalImageStorageService {

  /**
   * Allowed MIME types for rental images
   */
  private static final Set<String> ALLOWED_MIME = Set.of(
    "image/jpeg",
    "image/png",
    "image/webp"
  );

  /**
   * Maps MIME types to file extensions
   */
  private static final Map<String, String> EXTENSION_MAP = Map.of(
    "image/jpeg", ".jpg",
    "image/png", ".png",
    "image/webp", ".webp"
  );

  private final Tika tika;

  @Value("${app.upload.dir}")
  private String uploadDir;

  @Value("${app.upload.url}")
  private String uploadUrl;

  @Value("${app.upload.max-size}")
  private DataSize maxFileSize;

  private Path uploadRootPath;

  /**
   * Initializes the upload directory on application startup. Creates the directory if it doesn't
   * exist.
   */
  @PostConstruct
  void initializeUploadPath() {
    try {
      uploadRootPath = Paths.get(uploadDir).toAbsolutePath().normalize();
      Files.createDirectories(uploadRootPath);
    } catch (IOException e) {
      // Log and rethrow as unchecked exception to fail startup
      log.error("Failed to initialize upload directory: {}", uploadDir, e);
      throw new IllegalStateException(
        "Could not initialize upload directory: " + uploadRootPath, e);
    }
  }

  /**
   * Validates and saves a rental image to local storage. Performs multi-layered validation: empty
   * check, size limit, MIME type detection, and image integrity verification. Returns the URL path
   * for accessing the saved image.
   */
  public String saveRentalImage(MultipartFile file, Long rentalId) {
    try {
      // Validate file presence
      if (file == null || file.isEmpty()) {
        throw new IllegalArgumentException("File is empty");
      }

      // Validate file size
      if (file.getSize() > maxFileSize.toBytes()) {
        throw new IllegalArgumentException("File exceeds maximum allowed size");
      }

      // Read bytes once for reuse
      byte[] fileBytes = file.getBytes();

      // Detect actual MIME type by content inspection
      String mimeType = tika.detect(fileBytes);

      if (!ALLOWED_MIME.contains(mimeType)) {
        throw new IllegalArgumentException("Unsupported file type: " + mimeType);
      }

      // Verify image integrity (catches corrupted files)
      try (InputStream inputStream = file.getInputStream()) {
        if (ImageIO.read(inputStream) == null) {
          throw new IllegalArgumentException("Invalid or corrupted image file");
        }
      }

      // Generate unique filename with rental ID and timestamp
      String filename =
        "rental_" + rentalId + "_" + Instant.now().toEpochMilli() + EXTENSION_MAP.get(mimeType);

      // Save file to disk
      Path targetLocation = uploadRootPath.resolve(filename).normalize();
      Files.write(targetLocation, fileBytes, StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING);

      // Log successful upload with file details
      log.info("Saved rental image: {} ({} bytes, {})", filename, fileBytes.length, mimeType);

      // Return URL path, handling trailing slash
      return (uploadUrl.endsWith("/") ? uploadUrl : uploadUrl + "/") + filename;

    } catch (IOException e) {
      // Log and rethrow as unchecked exception
      log.error("Failed to save rental image for rental {}: {}", rentalId, e.getMessage(), e);
      throw new IllegalStateException("Failed to save rental image", e);
    }
  }
}
