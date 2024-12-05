package com.c0lap5o.JWTAuthenticationBackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a Role in the application.
 * This class is mapped to the "roles" table in the database.
 */
@Entity
@Table(name = "roles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

  /**
   * The unique identifier for the role.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * The name of the role, represented as an enumeration.
   * This field is stored as a string in the database with a maximum length of 20 characters.
   */
  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ERole name;
}