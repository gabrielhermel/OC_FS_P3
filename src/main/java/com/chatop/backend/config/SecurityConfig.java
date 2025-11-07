package com.chatop.backend.config;

import com.chatop.backend.security.JwtAuthenticationFilter;
import com.chatop.backend.security.JwtService;
import com.chatop.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures application security settings including JWT authentication.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtService jwtService;

  // Controls whether Swagger is publicly accessible, injected from application properties.
  @Value("${swagger.noauth}")
  private boolean swaggerNoAuth;

  /**
   * Registers the {@link JwtAuthenticationFilter} bean used to validate JWT tokens and populate the
   * authentication context. Declared separately to avoid circular dependencies.
   *
   * @param authService service used to retrieve user details for token validation. Injected lazily
   *                    to break circular reference.
   * @return a configured {@link JwtAuthenticationFilter} instance
   */
  @Bean
  public JwtAuthenticationFilter jwtAuthFilter(AuthService authService) {
    return new JwtAuthenticationFilter(jwtService, authService);
  }

  /**
   * Configures the HTTP security filter chain. Disables CSRF protection for stateless API and only
   * permits access to authorization endpoints (and conditionally Swagger) without authentication.
   * Integrates the JWT filter into the chain.
   *
   * @param httpSecurity  the HttpSecurity instance to configure
   * @param jwtAuthFilter the JWT authentication filter bean
   * @return the configured SecurityFilterChain
   * @throws Exception if configuration fails
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
    JwtAuthenticationFilter jwtAuthFilter) throws Exception {
    httpSecurity
      // Disable CSRF because JWTs are being used, not cookies
      .csrf(AbstractHttpConfigurer::disable)

      // Ensure that Spring Security never creates or uses an HTTP session
      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

      // Authorization rules
      .authorizeHttpRequests(auth -> {
        auth
          // Allow unauthenticated access to registration and login
          .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()

          // Require authentication for the /me endpoint
          .requestMatchers("/api/auth/me").authenticated();

        // Optionally allow unauthenticated access to Swagger UI and API docs
        if (swaggerNoAuth) {
          auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**").permitAll();
        }

        // Require authentication for any other request
        auth.anyRequest().authenticated();
      })

      // Custom exception handling for authentication and authorization failures
      .exceptionHandling(exceptions -> exceptions
        // Return 401 for unauthenticated requests
        .authenticationEntryPoint((request, response, authException) -> {
          response.setStatus(HttpStatus.UNAUTHORIZED.value());
          response.setContentType("application/json");
          response.getWriter().write("{}");
        })
        // Return 403 for forbidden access when user lacks permission
        .accessDeniedHandler((request, response, accessDeniedException) -> {
          response.setStatus(HttpStatus.FORBIDDEN.value());
          response.setContentType("application/json");
          response.getWriter().write("{}");
        })
      )

      // Add JWT filter before Spring’s built-in authentication filter
      .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();
  }


  /**
   * Provides a BCrypt password encoder bean for hashing passwords.
   *
   * @return BCryptPasswordEncoder instance
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
