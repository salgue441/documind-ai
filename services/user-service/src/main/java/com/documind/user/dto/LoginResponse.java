package com.documind.user.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for successful login responses.
 *
 * <p>Contains authentication tokens and user information returned after successful login.
 *
 * @author Carlos Salguero
 * @version 1.0
 * @since 2025-08-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

  /** The JWT access token for authenticated requests. */
  private String accessToken;

  /** The JWT refresh token for obtaining new access tokens. */
  private String refreshToken;

  /** The type of token (always "Bearer" in this implementation). Defaults to "Bearer". */
  private String tokenType = "Bearer";

  /** The expiration time of the access token in seconds. */
  private Long expiresIn;

  /** The authenticated user's information. */
  private UserDto user;

  /** The timestamp of when the login occurred. */
  private LocalDateTime loginTime;

  /**
   * Constructs a new LoginResponse with the current time as login time.
   *
   * @param accessToken the JWT access token
   * @param refreshToken the JWT refresh token
   * @param expiresIn expiration time in seconds
   * @param user the authenticated user's information
   */
  public LoginResponse(String accessToken, String refreshToken, Long expiresIn, UserDto user) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expiresIn = expiresIn;
    this.user = user;
    this.loginTime = LocalDateTime.now();
  }
}
