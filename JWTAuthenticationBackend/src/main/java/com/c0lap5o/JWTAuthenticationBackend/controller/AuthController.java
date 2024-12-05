package com.c0lap5o.JWTAuthenticationBackend.controller;

import com.c0lap5o.JWTAuthenticationBackend.dto.request.SignupRequest;
import com.c0lap5o.JWTAuthenticationBackend.model.ERole;
import com.c0lap5o.JWTAuthenticationBackend.model.Role;
import com.c0lap5o.JWTAuthenticationBackend.model.User;
import com.c0lap5o.JWTAuthenticationBackend.repository.RoleRepository;
import com.c0lap5o.JWTAuthenticationBackend.repository.UserRepository;
import com.c0lap5o.JWTAuthenticationBackend.dto.request.LoginRequest;
import com.c0lap5o.JWTAuthenticationBackend.dto.response.JwtResponse;
import com.c0lap5o.JWTAuthenticationBackend.dto.response.MessageResponse;
import com.c0lap5o.JWTAuthenticationBackend.security.jwt.JwtUtils;
import com.c0lap5o.JWTAuthenticationBackend.services.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller for handling user authentication and registration.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
@Slf4j
public class AuthController {

  /**
   * Authentication manager for handling user authentication.
   */
  @Autowired
  private AuthenticationManager authenticationManager;

  /**
   * Repository for user data.
   */
  @Autowired
  private UserRepository userRepository;

  /**
   * Repository for role data.
   */
  @Autowired
  private RoleRepository roleRepository;

  /**
   * Password encoder for encrypting user passwords.
   */
  @Autowired
  private PasswordEncoder encoder;

  /**
   * Utility for generating JWT tokens.
   */
  @Autowired
  private JwtUtils jwtUtils;

  /**
   * Authenticates a user and returns a JWT token.
   *
   * @param loginRequest The login request containing the username and password.
   * @return A ResponseEntity containing the JWT token and user details.
   */
  @Operation(summary = "Authenticate user", description = "Authenticate a user and return a JWT token")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successful authentication",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class))),
          @ApiResponse(responseCode = "401", description = "Unauthorized",
                  content = @Content(schema = @Schema(implementation = MessageResponse.class)))
  })
  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    // Authenticate the user using the authentication manager
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    // Set the authentication context
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Generate the JWT token
    String jwt = jwtUtils.generateJwtToken(authentication);

    // Get the user details from the authentication principal
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    // Collect the user's roles
    List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    log.info("User {} signed in", loginRequest.getUsername());

    // Return the JWT response
    return ResponseEntity.ok(JwtResponse.builder()
                    .token(jwt)
                    .id(userDetails.getId())
                    .username(userDetails.getUsername())
                    .roles(roles)
            .build());
  }

  /**
   * Registers a new user account.
   *
   * @param signUpRequest The signup request containing the user details.
   * @return A ResponseEntity indicating the registration status.
   */
  @Operation(summary = "Register new user", description = "Register a new user account")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "User registered successfully",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
          @ApiResponse(responseCode = "400", description = "Bad request - username or email already in use",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
  })
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    // Check if the username is already taken
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Username is already taken!"));
    }

    // Check if the email is already in use
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create a new user account
    User user = User.builder()
            .username(signUpRequest.getUsername())
            .email(signUpRequest.getEmail())
            .password(encoder.encode(signUpRequest.getPassword()))
            .build();

    // Set the user's roles
    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      // Default to the USER role if no roles are specified
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      // Assign the specified roles
      strRoles.forEach(role -> {
        switch (role) {
          case "admin":
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);
            break;
          case "mod":
            Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(modRole);
            break;
          default:
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);
    log.info("User Created: Username: {} Email: {} Role: {}", user.getUsername(), user.getEmail(), user.getRoles());
    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}