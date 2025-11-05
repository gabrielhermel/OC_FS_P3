package com.chatop.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the application. Configures HTTP security settings and authentication
 * requirements.
 */
@Configuration
public class SecurityConfig {

  /**
   * Configures the security filter chain. Disables CSRF protection for stateless API and only
   * permits access to authorization endpoints without authentication.
   *
   * @param httpSecurity the HttpSecurity to configure
   * @return the configured SecurityFilterChain
   * @throws Exception if configuration fails
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
      // Disable CSRF because JWTs are being used, not cookies
      .csrf(AbstractHttpConfigurer::disable)

      // Ensure that Spring Security never creates or uses an HTTP session
      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

      // Authorization rules
      .authorizeHttpRequests(auth -> auth
        // Allow unauthenticated access to authentication endpoints
        .requestMatchers("/api/auth/**").permitAll()
        // Require authentication for everything else
        .anyRequest().authenticated()
      );

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
