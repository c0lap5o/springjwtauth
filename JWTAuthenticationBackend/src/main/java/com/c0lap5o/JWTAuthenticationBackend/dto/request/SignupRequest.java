package com.c0lap5o.JWTAuthenticationBackend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * Request class for handling user signup requests.
 * This class contains the necessary fields for a signup request and uses Lombok for simplifying the code.
 */
@Data
@Builder
public class SignupRequest {

  /**
   * The username of the user signing up.
   * This field is required, must be between 3 and 20 characters long.
   */
  @NotBlank
  @Size(min = 3, max = 20)
  private String username;

  /**
   * The email address of the user signing up.
   * This field is required, must be a valid email address, and must not exceed 50 characters.
   */
  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  /**
   * The roles to be assigned to the user.
   * This is an optional field and can contain a set of role names (e.g., "admin", "mod", "user").
   */
  private Set<String> role;

  /**
   * The password of the user signing up.
   * This field is required, must be between 6 and 40 characters long.
   */
  @NotBlank
  @Size(min = 6, max = 40)
  private String password;
}