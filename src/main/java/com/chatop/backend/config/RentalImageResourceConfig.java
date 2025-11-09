package com.chatop.backend.config;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures serving of uploaded rental images as static resources.
 * Maps the local upload directory to a URL path, enabling browser caching
 * and access to uploaded files without application restart.
 */
@Configuration
public class RentalImageResourceConfig implements WebMvcConfigurer {

  @Value("${app.upload.dir}")
  private String uploadDir;

  @Value("${app.upload.url}")
  private String uploadUrl;

  @Value("${app.upload.cache-seconds}")
  private long cacheSeconds;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // Convert upload directory to absolute file URI
    String absolutePath = Paths.get(uploadDir).toAbsolutePath().normalize().toUri().toString();

    // Ensure URL pattern ends with /** for wildcard matching
    String urlPattern = uploadUrl.endsWith("/") ? uploadUrl + "**" : uploadUrl + "/**";

    // Register resource handler with caching
    registry.addResourceHandler(urlPattern)
      .addResourceLocations(absolutePath)
      .setCacheControl(CacheControl.maxAge(cacheSeconds, TimeUnit.SECONDS).cachePublic());
  }
}
