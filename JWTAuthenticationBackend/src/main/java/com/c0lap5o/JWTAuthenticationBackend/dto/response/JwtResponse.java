package com.c0lap5o.JWTAuthenticationBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response class for JWT authentication, containing the token and user details.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

  /**
   * The JWT token generated for the user.
   */
  private String token;

  /**
   * The type of the token, which is always "Bearer" for JWT tokens.
   */
  private String type = "Bearer";

  /**
   * The ID of the authenticated user.
   */
  private Long id;

  /**
   * The username of the authenticated user.
   */
  private String username;

  /**
   * The email address of the authenticated user.
   */
  private String email;

  /**
   * A list of roles assigned to the authenticated user.
   */
  private List<String> roles;

}