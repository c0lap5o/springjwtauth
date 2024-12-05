package com.c0lap5o.JWTAuthenticationBackend.unit.controller;

import com.c0lap5o.JWTAuthenticationBackend.controller.AuthController;
import com.c0lap5o.JWTAuthenticationBackend.model.ERole;
import com.c0lap5o.JWTAuthenticationBackend.model.Role;
import com.c0lap5o.JWTAuthenticationBackend.model.User;
import com.c0lap5o.JWTAuthenticationBackend.repository.RoleRepository;
import com.c0lap5o.JWTAuthenticationBackend.repository.UserRepository;
import com.c0lap5o.JWTAuthenticationBackend.dto.request.LoginRequest;
import com.c0lap5o.JWTAuthenticationBackend.dto.request.SignupRequest;
import com.c0lap5o.JWTAuthenticationBackend.security.jwt.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerUnitTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        // Mocking Role Repository Behaviour
        Role userRole = Role.builder()
                .name(ERole.ROLE_USER)
                .id(1)
                .build();
        Role moderatorRole = Role.builder()
                .name(ERole.ROLE_MODERATOR)
                .id(2)
                .build();

        Role adminRole = Role.builder()
                .name(ERole.ROLE_ADMIN)
                .id(3)
                .build();

        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));
        when(roleRepository.findByName(ERole.ROLE_MODERATOR)).thenReturn(Optional.of(moderatorRole));
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));

        // Mocking User repository behavior
        List<User> users = new ArrayList<>();
        users.add(User.builder().
                email("regularuser@email.com")
                .username("regular_user")
                .password("regularUser123")
                .roles(new HashSet<>(List.of(userRole)))
                .build());


        users.add(User.builder()
                .email("moderatoruser@email.com")
                .username("moderator_user")
                .password("moderatorPass456")
                .roles(new HashSet<>(List.of(moderatorRole)))
                .build());
        users.add(User.builder()
                .email("adminuser@email.com")
                .username("admin_user")
                .password("adminPass789")
                .roles(new HashSet<>(List.of(adminRole)))
                .build());

        when(userRepository.findAll()).thenReturn(users);
    }

    @Test
    public void testUsersExist() {
        // Test that users are available
        List<User> users = userRepository.findAll();
        long userCount = users.size();
        assertTrue(userCount >= 3, "Expected at least 3 users, but found " + userCount);
    }

    @Test
    public void testSuccessfulLogin() throws Exception {
        // Given
        LoginRequest loginRequest = LoginRequest.builder()
                .username("regular_user")
                .password("regularUser123")
                .build();
        when(authenticationManager.authenticate(any())).thenReturn(null);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk());

        // Then
        // You can add additional assertions here if needed
    }

    @Test
    public void testSuccessfulSignup() throws Exception {
        // Given
        SignupRequest signupRequest = SignupRequest.builder()
                .username("newuser")
                .email("newuser@example.com")
                .password("newuser123")
                .role(new HashSet<>(List.of(ERole.ROLE_USER.name())))
                .build();

        // Mocking role repository behavior
        Role userRole = Role.builder()
                .name(ERole.ROLE_USER)
                .id(1)
                .build();
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));

        // Mocking user repository behavior to check for existing users
        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signupRequest)))
                .andExpect(status().isOk());

        // Then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testFailedSignupDueToExistingUsername() throws Exception {
        // Given
        SignupRequest signupRequest = SignupRequest.builder()
                .username("regular_user")
                .email("newuser@example.com")
                .password("newuser123")
                .role(new HashSet<>(List.of(ERole.ROLE_USER.name())))
                .build();

        // Mocking role repository behavior
        Role userRole = Role.builder()
                .name(ERole.ROLE_USER)
                .id(1)
                .build();
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));

        // Mocking user repository behavior to check for existing users
        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(true);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());

        // Then
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testFailedSignupDueToExistingEmail() throws Exception {
        // Given
        SignupRequest signupRequest = SignupRequest.builder()
                .username("newuser")
                .email("regularuser@email.com")
                .password("newuser123")
                .role(new HashSet<>(List.of(ERole.ROLE_USER.name())))
                .build();

        // Mocking role repository behavior
        Role userRole = Role.builder()
                .name(ERole.ROLE_USER)
                .id(1)
                .build();
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));

        // Mocking user repository behavior to check for existing users
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());

        // Then
        verify(userRepository, never()).save(any(User.class));
    }
}