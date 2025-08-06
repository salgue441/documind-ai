package com.documind.user.service;

import com.documind.user.dto.LoginRequest;
import com.documind.user.dto.LoginResponse;

/**
 * Service interface for authentication and authorization operations.
 *
 * <p>Provides methods for user authentication, token management, and account locking/unlocking
 * functionality.
 *
 * @author Carlos Salguero
 * @version 1.0
 * @since 2025-08-06
 */
public interface AuthService {

  /**
   * Authenticates a user and generates access/refresh tokens.
   *
   * @param loginRequest the login credentials
   * @return the authentication response containing tokens
   */
  LoginResponse login(LoginRequest loginRequest);

  /**
   * Refreshes an access token using a valid refresh token.
   *
   * @param refreshToken the refresh token
   * @return new authentication tokens
   */
  LoginResponse refreshToken(String refreshToken);

  /**
   * Invalidates the provided token (logout operation).
   *
   * @param token the token to invalidate
   */
  void logout(String token);

  /**
   * Validates the integrity and expiration of a token.
   *
   * @param token the token to validate
   * @return true if the token is valid, false otherwise
   */
  boolean validateToken(String token);

  /**
   * Locks a user account to prevent login attempts.
   *
   * @param usernameOrEmail the username or email of the account to lock
   */
  void lockAccount(String usernameOrEmail);

  /**
   * Unlocks a previously locked user account.
   *
   * @param usernameOrEmail the username or email of the account to unlock
   */
  void unlockAccount(String usernameOrEmail);
}
