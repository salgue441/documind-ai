package com.documind.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object for user login requests.
 *
 * <p>Contains the credentials required for user authentication, including username/email and
 * password, with an optional remember-me flag.
 *
 * @author Carlos Salguero
 * @version 1.0
 * @since 2025-08-06
 */
@Data
public class LoginRequest {

  /** The username or email address for authentication. Cannot be blank. */
  @NotBlank(message = "Username or email is required")
  private String usernameOrEmail;

  /** The user's password for authentication. Cannot be blank. */
  @NotBlank(message = "Password is required")
  private String password;

  /**
   * Flag indicating whether the authentication should be persisted beyond the current session.
   * Defaults to false.
   */
  private boolean rememberMe = false;
}
