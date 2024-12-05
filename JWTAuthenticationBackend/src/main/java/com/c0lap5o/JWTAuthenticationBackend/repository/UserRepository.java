package com.c0lap5o.JWTAuthenticationBackend.repository;

import com.c0lap5o.JWTAuthenticationBackend.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing User entities.
 * This interface extends JpaRepository to provide basic CRUD operations.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Checks if a user with the given username already exists in the database.
     *
     * @param username The username to check.
     * @return True if a user with the given username exists, false otherwise.
     */
    boolean existsByUsername(@NotBlank String username);

    /**
     * Checks if a user with the given email already exists in the database.
     *
     * @param email The email to check.
     * @return True if a user with the given email exists, false otherwise.
     */
    boolean existsByEmail(@NotBlank @Size(max = 50) @Email String email);

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user to find.
     * @return An Optional containing the User if found, or an empty Optional otherwise.
     */
    Optional<User> findByUsername(String username);
}