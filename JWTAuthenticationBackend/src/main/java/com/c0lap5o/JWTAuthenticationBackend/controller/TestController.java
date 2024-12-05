package com.c0lap5o.JWTAuthenticationBackend.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * Controller for testing different user roles and access levels.
 * This controller is only active in the 'dev' profile.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
@Tag(name = "Test", description = "Test endpoints for different user roles")
@Profile("dev")
public class TestController {

  /**
   * Returns public content accessible to all users.
   *
   * @return A string indicating public content.
   */
  @Operation(summary = "Get public content", description = "Accessible to all users")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully retrieved public content")
  })
  @GetMapping("/all")
  public String allAccess() {
    // Return a message indicating public content
    return "Public Content.";
  }

  /**
   * Returns user content accessible to users with the USER, MODERATOR, or ADMIN role.
   *
   * @return A string indicating user content.
   */
  @Operation(summary = "Get user content", description = "Accessible to users with USER, MODERATOR, or ADMIN role")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully retrieved user content"),
          @ApiResponse(responseCode = "403", description = "Access denied")
  })
  @SecurityRequirement(name = "bearerAuth")  // Specify the security requirement
  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public String userAccess() {
    // Return a message indicating user content
    return "User Content.";
  }

  /**
   * Returns moderator content accessible only to users with the MODERATOR role.
   *
   * @return A string indicating moderator content.
   */
  @Operation(summary = "Get moderator content", description = "Accessible to users with MODERATOR role")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully retrieved moderator content"),
          @ApiResponse(responseCode = "403", description = "Access denied")
  })
  @SecurityRequirement(name = "bearerAuth")  // Specify the security requirement
  @GetMapping("/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() {
    // Return a message indicating moderator content
    return "Moderator Board.";
  }

  /**
   * Returns admin content accessible only to users with the ADMIN role.
   *
   * @return A string indicating admin content.
   */
  @Operation(summary = "Get admin content", description = "Accessible to users with ADMIN role")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Successfully retrieved admin content"),
          @ApiResponse(responseCode = "403", description = "Access denied")
  })
  @SecurityRequirement(name = "bearerAuth")  // Specify the security requirement
  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    // Return a message indicating admin content
    return "Admin Board.";
  }
}