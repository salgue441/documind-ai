package com.documind.user.dto;

import com.documind.common.enums.UserRole;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

/**
 * Data Transfer Object for user information.
 *
 * <p>Contains user details suitable for sending to clients, excluding sensitive information like
 * passwords.
 *
 * @author Carlos Salguero
 * @version 1.0
 * @since 2025-08-06
 */
@Data
public class UserDto {

  /** The unique identifier for the user. */
  private UUID id;

  /** The user's username. */
  private String username;

  /** The user's email address. */
  private String email;

  /** The user's first name. */
  private String firstName;

  /** The user's last name. */
  private String lastName;

  /** The user's full name (combination of first and last names). */
  private String fullName;

  /** The user's role in the system. */
  private UserRole role;

  /** Flag indicating whether the user account is enabled. */
  private Boolean isEnabled;

  /** Timestamp of the user's last successful login. */
  private LocalDateTime lastLoginAt;

  /** Timestamp of when the user account was created. */
  private LocalDateTime createdAt;

  /** Timestamp of when the user account was last updated. */
  private LocalDateTime updatedAt;

  /**
   * Returns the display name for the user, preferring full name if available.
   *
   * @return the display name (full name if available, otherwise username)
   */
  public String getDisplayName() {
    if (fullName != null && !fullName.trim().isEmpty()) {
      return fullName;
    }
    return username;
  }
}
