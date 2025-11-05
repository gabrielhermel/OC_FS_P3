package com.chatop.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//Service for generating and validating JWT tokens.
@Service
public class JwtService {

  // Secret key for signing JWTs, injected from application properties.
  @Value("${jwt.secret}")
  private String secretKey;

  // JWT expiration time in milliseconds, injected from application properties.
  @Value("${jwt.expiration}")
  private long jwtExpiration;

  /**
   * Retrieves the signing key from the base64-encoded secret.
   *
   * @return the HMAC signing key
   */
  private Key getSigningKey() {
    byte[] keyBytes = Base64.getDecoder().decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Generates a JWT token with custom claims and username.
   *
   * @param claims   additional claims to include in the token
   * @param username the subject (username) of the token
   * @return the generated JWT token string
   */
  public String generateToken(Map<String, Object> claims, String username) {
    return Jwts.builder()
      .setClaims(claims)
      .setSubject(username)
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
      .signWith(getSigningKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  /**
   * Extracts the username from a JWT token.
   *
   * @param token the JWT token
   * @return the username (subject) from the token
   */
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Extracts a specific claim from a JWT token.
   *
   * @param token          the JWT token
   * @param claimsResolver function to extract the desired claim
   * @return the extracted claim value
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims =
      Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    return claimsResolver.apply(claims);
  }

  /**
   * Validates a JWT token against a username and expiration.
   *
   * @param token    the JWT token to validate
   * @param username the expected username
   * @return true if token is valid and not expired, false otherwise
   */
  public boolean isTokenValid(String token, String username) {
    return username.equals(extractUsername(token)) && !isTokenExpired(token);
  }

  /**
   * Checks if a JWT token has expired.
   *
   * @param token the JWT token
   * @return true if token is expired, false otherwise
   */
  private boolean isTokenExpired(String token) {
    return extractClaim(token, Claims::getExpiration).before(new Date());
  }
}
