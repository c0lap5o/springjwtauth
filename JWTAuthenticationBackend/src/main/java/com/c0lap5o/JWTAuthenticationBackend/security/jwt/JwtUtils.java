package com.c0lap5o.JWTAuthenticationBackend.security.jwt;

import com.c0lap5o.JWTAuthenticationBackend.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utility class for handling JWT tokens, including generation, validation, and extraction of user details.
 */
@Slf4j
@Component
public class JwtUtils {

  /**
   * The secret key used for signing and verifying JWT tokens.
   */
  @Value("${app.jwtSecret}")
  private String jwtSecret;

  /**
   * The expiration time in milliseconds for JWT tokens.
   */
  @Value("${app.jwtExpirationMs}")
  private int jwtExpirationMs;

  /**
   * Generates a JWT token for the given authentication object.
   *
   * @param authentication The authentication object containing the user details.
   * @return The generated JWT token.
   */
  public String generateJwtToken(Authentication authentication) {
    // Extract the user principal from the authentication object
    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

    // Build and return the JWT token
    return Jwts.builder()
            .subject(userPrincipal.getUsername()) // Set the subject (username)
            .issuedAt(new Date()) // Set the issue time
            .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // Set the expiration time
            .signWith(key()) // Sign the token with the secret key
            .compact(); // Compact the token
  }

  /**
   * Returns the secret key used for signing and verifying JWT tokens.
   *
   * @return The secret key.
   */
  private SecretKey key() {
    // Decode the secret key from base64 and return it as a HMAC SHA key
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  /**
   * Extracts the username from the given JWT token.
   *
   * @param token The JWT token.
   * @return The username extracted from the token.
   */
  public String getUserNameFromJwtToken(String token) {
    // Verify the token and extract the subject (username)
    return Jwts.parser()
            .verifyWith(key())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
  }

  /**
   * Validates the given JWT token.
   *
   * @param authToken The JWT token to validate.
   * @return True if the token is valid, false otherwise.
   */
  public boolean validateJwtToken(String authToken) {
    try {
      // Verify the token using the secret key
      Jwts.parser().verifyWith(key()).build().parse(authToken);
      return true; // Token is valid
    } catch (MalformedJwtException e) {
      // Log error for malformed JWT token
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      // Log error for expired JWT token
      log.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      // Log error for unsupported JWT token
      log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      // Log error for empty JWT claims string
      log.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false; // Token is invalid
  }
}