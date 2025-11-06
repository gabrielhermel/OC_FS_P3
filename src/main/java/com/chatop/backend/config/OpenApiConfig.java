package com.chatop.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration for OpenAPI/Swagger documentation. */
@Configuration
public class OpenApiConfig {

  /**
   * Configures OpenAPI documentation metadata.
   *
   * @return OpenAPI instance with application info
   */
  @Bean
  public OpenAPI chatopOpenAPI() {
    return new OpenAPI()
      .info(
        new Info()
          .title("ChâTop REST API")
          .description("Backend API documentation for the ChâTop rental application")
          .version("1.0.0"));
  }
}
