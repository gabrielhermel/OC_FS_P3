package com.chatop.backend.security;

import com.chatop.backend.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/** Filter that validates JWT tokens from incoming requests and sets authentication context. */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final AuthService authService;

  /**
   * Intercepts incoming requests, extracts and validates JWT tokens, and sets the authentication
   * context if the token is valid.
   *
   * @param request     the incoming HTTP request
   * @param response    the HTTP response to be sent back
   * @param filterChain the Spring Security filter chain
   * @throws ServletException if an internal servlet error occurs
   * @throws IOException      if an I/O error occurs during processing
   */
  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain) throws ServletException, IOException {

    // Extract Authorization header
    final String authHeader = request.getHeader("Authorization");

    // Check if header is present and starts with "Bearer "
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    final String jwt = authHeader.substring(7);
    final String userEmail = jwtService.extractUsername(jwt);

    // Check if user is not already authenticated
    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = authService.findByEmail(userEmail).orElse(null);
      if (userDetails != null && jwtService.isTokenValid(jwt, userEmail)) {
        // Set authentication in the security context
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    // Continue the filter chain
    filterChain.doFilter(request, response);
  }

}
