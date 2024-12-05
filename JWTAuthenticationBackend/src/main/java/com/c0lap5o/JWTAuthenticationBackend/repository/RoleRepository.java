package com.c0lap5o.JWTAuthenticationBackend.repository;

import com.c0lap5o.JWTAuthenticationBackend.model.ERole;
import com.c0lap5o.JWTAuthenticationBackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing Role entities.
 * This interface extends JpaRepository to provide basic CRUD operations.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  /**
   * Finds a Role by its name.
   *
   * @param name The name of the role to find (e.g., ROLE_USER, ROLE_MODERATOR, ROLE_ADMIN).
   * @return An Optional containing the Role if found, or an empty Optional otherwise.
   */
  Optional<Role> findByName(ERole name);
}