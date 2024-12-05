package com.c0lap5o.JWTAuthenticationBackend.dataloader;

import com.c0lap5o.JWTAuthenticationBackend.model.ERole;
import com.c0lap5o.JWTAuthenticationBackend.model.Role;
import com.c0lap5o.JWTAuthenticationBackend.model.User;
import com.c0lap5o.JWTAuthenticationBackend.repository.RoleRepository;
import com.c0lap5o.JWTAuthenticationBackend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * DataLoader component responsible for initializing test data in the development environment.
 * This component is only active in the 'dev' profile.
 */
@Component
@Slf4j
@Profile("dev")
public class DataLoader implements CommandLineRunner {


    /**
     * Repository for user data.
     */
    private final UserRepository userRepository;

    /**
     * Password encoder for encrypting user passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Repository for role data.
     */
    private final RoleRepository roleRepository;

    /**
     * Constructor to inject dependencies.
     *
     * @param userRepository Repository for user data.
     * @param passwordEncoder Password encoder for encrypting user passwords.
     * @param roleRepository Repository for role data.
     */
    @Autowired
    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    /**
     * Method to run when the application starts. It initializes test data if none exists.
     *
     * @param args Command line arguments (not used).
     * @throws Exception If any error occurs during data loading.
     */
    @Override
    public void run(String... args) throws Exception {
        log.warn("DataLoader is running...");

        // Retrieve roles from the database
        Optional<Role> userRole = roleRepository.findByName(ERole.ROLE_USER);
        Optional<Role> moderatorRole = roleRepository.findByName(ERole.ROLE_MODERATOR);
        Optional<Role> adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);

        // Check if roles exist in the database
        if (userRole.isEmpty()) {
            log.error("Error getting user role from database, please check the roles table");
        }

        if (moderatorRole.isEmpty()) {
            log.error("Error getting moderator role from database, please check the roles table");
        }

        if (adminRole.isEmpty()) {
            log.error("Error getting admin role from database, please check the roles table");
        }

        // Check if users already exist in the database
        if (userRepository.count() == 0) { // Use count() instead of findAll().size()
            // Create a set to hold roles
            Set<Role> roles = new HashSet<>();

            // Add user role and create a test user
            roles.add(userRole.get());
            User user1 = User.builder()
                    .username("john_doe")
                    .email("john.doe@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .roles(roles)
                    .build();

            // Log user creation
            log.info("Creating user: username={}, email={}, password={}, role={}", user1.getUsername(), user1.getEmail(), "password123", "User");

            // Remove user role and add moderator role
            roles.remove(userRole.get());
            roles.add(moderatorRole.get());
            User user2 = User.builder()
                    .username("jane_smith")
                    .email("jane.smith@example.com")
                    .password(passwordEncoder.encode("securePass456"))
                    .roles(roles)
                    .build();

            // Log user creation
            log.info("Creating user: username={}, email={}, password={}, role={}", user2.getUsername(), user2.getEmail(), "securePass456", "Moderator");

            // Remove moderator role and add admin role
            roles.remove(moderatorRole.get());
            roles.add(adminRole.get());
            User user3 = User.builder()
                    .username("admin_user")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("adminPass789"))
                    .roles(roles)
                    .build();

            // Log user creation
            log.info("Creating user: username={}, email={}, password={}, role={}", user3.getUsername(), user3.getEmail(), "adminPass789", "Admin");

            // Save users to the database
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

            log.info("Test users have been saved to the database.");
        } else {
            log.info("Users already exist in the database. No new users added.");
        }
    }
}