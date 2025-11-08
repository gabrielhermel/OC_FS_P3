package com.chatop.backend.annotation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Common error responses for GET (all) requests to secured endpoints.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
  @ApiResponse(
    responseCode = "401",
    description = "Unauthorized or missing JWT",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(example = "{}"))
  ),
  @ApiResponse(
    responseCode = "403",
    description = "Forbidden",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(example = "{}"))
  )
})
public @interface GetAllErrorResponses {

}
