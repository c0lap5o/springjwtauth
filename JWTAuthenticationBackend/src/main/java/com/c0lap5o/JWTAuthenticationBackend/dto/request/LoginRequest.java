package com.c0lap5o.JWTAuthenticationBackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request class for handling user login requests.
 * This class contains the necessary fields for a login request and uses Lombok for simplifying the code.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

	/**
	 * The username of the user attempting to log in.
	 * This field is required and cannot be blank.
	 */
	@NotBlank
	private String username;

	/**
	 * The password of the user attempting to log in.
	 * This field is required and cannot be blank.
	 */
	@NotBlank
	private String password;
}