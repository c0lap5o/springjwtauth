package com.c0lap5o.JWTAuthenticationBackend.services;

import com.c0lap5o.JWTAuthenticationBackend.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of the UserDetails interface to represent a user's details for Spring Security.
 */
public class UserDetailsImpl implements UserDetails {

  /**
   * Serial version UID for serialization purposes.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The ID of the user.
   */
  @Getter
  private Long id;

  /**
   * The username of the user.
   */
  private String username;

  /**
   * The email address of the user.
   */
  @Getter
  private String email;

  /**
   * The password of the user. This field is ignored by Jackson for serialization.
   */
  @JsonIgnore
  private String password;

  /**
   * The collection of authorities (roles) assigned to the user.
   */
  private Collection<? extends GrantedAuthority> authorities;

  /**
   * Constructor to initialize the UserDetailsImpl object with the given parameters.
   *
   * @param id The ID of the user.
   * @param username The username of the user.
   * @param email The email address of the user.
   * @param password The password of the user.
   * @param authorities The collection of authorities (roles) assigned to the user.
   */
  public UserDetailsImpl(Long id, String username, String email, String password,
                         Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
  }

  /**
   * Static method to build a UserDetailsImpl object from a User entity.
   *
   * @param user The User entity to build from.
   * @return A UserDetailsImpl object representing the user's details.
   */
  public static UserDetailsImpl build(User user) {
    // Convert the user's roles to a list of GrantedAuthority objects
    List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName().name()))
            .collect(Collectors.toList());

    // Create and return the UserDetailsImpl object
    return new UserDetailsImpl(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            authorities);
  }

  /**
   * Returns the collection of authorities (roles) assigned to the user.
   *
   * @return The collection of authorities.
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  /**
   * Returns the password of the user.
   *
   * @return The password.
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * Returns the username of the user.
   *
   * @return The username.
   */
  @Override
  public String getUsername() {
    return username;
  }

  /**
   * Checks if the user's account is not expired.
   *
   * @return True if the account is not expired, false otherwise.
   */
  @Override
  public boolean isAccountNonExpired() {
    return true; // Assume accounts are never expired for simplicity
  }

  /**
   * Checks if the user's account is not locked.
   *
   * @return True if the account is not locked, false otherwise.
   */
  @Override
  public boolean isAccountNonLocked() {
    return true; // Assume accounts are never locked for simplicity
  }

  /**
   * Checks if the user's credentials are not expired.
   *
   * @return True if the credentials are not expired, false otherwise.
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true; // Assume credentials are never expired for simplicity
  }

  /**
   * Checks if the user's account is enabled.
   *
   * @return True if the account is enabled, false otherwise.
   */
  @Override
  public boolean isEnabled() {
    return true; // Assume accounts are always enabled for simplicity
  }

  /**
   * Overrides the equals method to compare two UserDetailsImpl objects based on their IDs.
   *
   * @param o The object to compare with.
   * @return True if the objects are equal, false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  }
}