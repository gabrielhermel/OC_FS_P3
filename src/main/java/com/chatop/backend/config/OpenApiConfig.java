package com.chatop.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OpenAPI/Swagger documentation. Sets up JWT Bearer authentication for the
 * Swagger UI.
 */
@Configuration
public class OpenApiConfig {

  /**
   * Configures OpenAPI documentation metadata and security scheme for JWT. Defines a "bearerAuth"
   * security scheme that allows testing secured endpoints directly from the Swagger UI by providing
   * a JWT token. Note: Security requirements must be added per-endpoint using @SecurityRequirement
   * annotation.
   *
   * @return OpenAPI instance with API info and security configuration
   */
  @Bean
  public OpenAPI chatopOpenAPI() {
    final String securitySchemeName = "bearerAuth";

    return new OpenAPI()
      .info(
        new Info()
          .title("ChâTop REST API")
          .description("Backend API documentation for the ChâTop rental application")
          .version("1.0.0"))
      // Define the Bearer authentication scheme (JWT)
      .components(
        new Components()
          .addSecuritySchemes(
            securitySchemeName,
            new SecurityScheme()
              .name(securitySchemeName)
              .type(SecurityScheme.Type.HTTP)
              .scheme("bearer")
              .bearerFormat("JWT")));
  }
}
