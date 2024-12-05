package com.c0lap5o.JWTAuthenticationBackend.security.jwt;

import com.c0lap5o.JWTAuthenticationBackend.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter class to handle JWT token authentication for each incoming request.
 * This filter extends OncePerRequestFilter to ensure it runs once per request.
 */
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

  /**
   * Utility class for handling JWT tokens.
   */
  @Autowired
  private JwtUtils jwtUtils;

  /**
   * Service class for loading user details.
   */
  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  /**
   * Method to perform the actual filtering of the request.
   * This method checks for the presence of a valid JWT token in the Authorization header and sets the authentication context accordingly.
   *
   * @param request The HttpServletRequest object.
   * @param response The HttpServletResponse object.
   * @param filterChain The FilterChain to continue the request processing.
   * @throws ServletException If a servlet-related error occurs.
   * @throws IOException If an I/O error occurs.
   */
  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
          throws ServletException, IOException {
    try {
      // Extract the JWT token from the Authorization header
      String jwt = parseJwt(request);

      // Check if the JWT token is present and valid
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        // Extract the username from the JWT token
        String username = jwtUtils.getUserNameFromJwtToken(jwt);

        // Load the user details based on the username
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Create an authentication token with the user details and authorities
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

        // Set the authentication details from the request
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Set the authentication context
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      // Log any exceptions that occur during the authentication process
      log.error("Cannot set user authentication: {}", e);
    }

    // Continue the request processing with the next filter in the chain
    filterChain.doFilter(request, response);
  }

  /**
   * Method to parse the JWT token from the Authorization header of the request.
   *
   * @param request The HttpServletRequest object.
   * @return The extracted JWT token if present, otherwise null.
   */
  private String parseJwt(HttpServletRequest request) {
    // Get the Authorization header from the request
    String headerAuth = request.getHeader("Authorization");

    // Check if the header is present and starts with "Bearer "
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      // Extract the JWT token by removing the "Bearer " prefix
      return headerAuth.substring(7);
    }

    // Return null if the header is not present or does not start with "Bearer "
    return null;
  }
}