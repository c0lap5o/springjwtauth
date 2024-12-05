package com.c0lap5o.JWTAuthenticationBackend.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the AuthenticationEntryPoint interface to handle unauthorized access attempts.
 * This class is responsible for returning a JSON response when an authentication exception occurs.
 */
@Slf4j
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

  /**
   * Method to commence the authentication process when an authentication exception occurs.
   * This method sets the HTTP response status to 401 (Unauthorized) and returns a JSON error message.
   *
   * @param request The HttpServletRequest object.
   * @param response The HttpServletResponse object.
   * @param authException The AuthenticationException that triggered this method.
   * @throws IOException If an I/O error occurs.
   * @throws ServletException If a servlet-related error occurs.
   */
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
          throws IOException, ServletException {
    // Log the authentication exception
    log.error("Unauthorized error: {}", authException.getMessage());

    // Set the response content type to JSON
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    // Set the response status to 401 (Unauthorized)
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    // Create a map to hold the error response body
    final Map<String, Object> body = new HashMap<>();
    body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
    body.put("error", "Unauthorized");
    body.put("message", authException.getMessage());
    body.put("path", request.getServletPath());

    // Use ObjectMapper to convert the map to JSON and write it to the response output stream
    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), body);
  }
}