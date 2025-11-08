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
 * Common error responses for POST requests to unsecured (public access) endpoints.
 * Examples: /api/auth/register, /api/auth/login
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
  @ApiResponse(
    responseCode = "400",
    description = "Invalid input or missing fields",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(example = "{}"))
  )
})
public @interface PostUnsecuredErrorResponses {

}
