package com.c0lap5o.JWTAuthenticationBackend.security;

import com.c0lap5o.JWTAuthenticationBackend.security.jwt.AuthEntryPointJwt;
import com.c0lap5o.JWTAuthenticationBackend.security.jwt.AuthTokenFilter;
import com.c0lap5o.JWTAuthenticationBackend.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for Spring Security settings.
 * This class enables method security and configures the security filter chain.
 */
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

  /**
   * Array of endpoints that are allowed without authentication.
   */
  private final String[] allowedEndpoints = {
          "/api/auth/**",
          "/api/test/**",
          "/swagger-ui/**",
          "/v3/api-docs/**",
          "/api-docs/**"
  };

  /**
   * Service class for loading user details.
   */
  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  /**
   * Handler for unauthorized access attempts.
   */
  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  /**
   * Bean to create an instance of the AuthTokenFilter.
   *
   * @return An instance of the AuthTokenFilter.
   */
  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  /**
   * Bean to create a DaoAuthenticationProvider instance.
   * This provider uses the UserDetailsServiceImpl and a BCryptPasswordEncoder.
   *
   * @return A DaoAuthenticationProvider instance.
   */
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    // Set the user details service
    authProvider.setUserDetailsService(userDetailsService);

    // Set the password encoder
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  /**
   * Bean to create an AuthenticationManager instance.
   *
   * @param authConfig The AuthenticationConfiguration to get the AuthenticationManager from.
   * @return The AuthenticationManager instance.
   * @throws Exception If any error occurs during the creation of the AuthenticationManager.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  /**
   * Bean to create a PasswordEncoder instance using BCrypt.
   *
   * @return A BCryptPasswordEncoder instance.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Bean to configure the SecurityFilterChain.
   * This method sets up the security configuration, including CSRF, exception handling, session management, and authorization.
   *
   * @param http The HttpSecurity object to configure.
   * @return The configured SecurityFilterChain.
   * @throws Exception If any error occurs during the configuration.
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    // Disable CSRF protection
    http.csrf(AbstractHttpConfigurer::disable)

            // Set the authentication entry point for unauthorized access
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))

            // Set the session creation policy to STATELESS
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Configure authorization for HTTP requests
            .authorizeHttpRequests(auth ->
                    auth.requestMatchers(allowedEndpoints).permitAll() // Allow specified endpoints without authentication
                            .anyRequest().authenticated() // Authenticate all other requests
            );

    // Add the authentication provider
    http.authenticationProvider(authenticationProvider());

    // Add the AuthTokenFilter before the UsernamePasswordAuthenticationFilter
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}