package com.c0lap5o.JWTAuthenticationBackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a User in the application.
 * This class is mapped to the "users" table in the database.
 */
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

  /**
   * The unique identifier for the user.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * The username of the user.
   * This field is required and cannot be blank, with a maximum length of 20 characters.
   */
  @NotBlank
  @Size(max = 20)
  private String username;

  /**
   * The email address of the user.
   * This field is required, must be a valid email, and cannot exceed 50 characters.
   */
  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  /**
   * The password of the user.
   * This field is required and cannot be blank, with a maximum length of 120 characters.
   */
  @NotBlank
  @Size(max = 120)
  private String password;

  /**
   * The roles assigned to the user.
   * This field establishes a many-to-many relationship with the Role entity.
   */
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();
}