package com.c0lap5o.JWTAuthenticationBackend.services;

import com.c0lap5o.JWTAuthenticationBackend.model.User;
import com.c0lap5o.JWTAuthenticationBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the UserDetailsService interface for loading user-specific data.
 * This service is used by Spring Security to retrieve user details during authentication.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  /**
   * Repository for accessing user data.
   */
  @Autowired
  private UserRepository userRepository;

  /**
   * Loads a user by their username.
   *
   * @param username The username of the user to load.
   * @return UserDetails object containing user information.
   * @throws UsernameNotFoundException If the user is not found with the given username.
   */
  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Retrieve the user from the repository
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    // Build and return the UserDetailsImpl object from the User entity
    return UserDetailsImpl.build(user);
  }
}