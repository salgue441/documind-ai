package com.documind.user.entity;

import com.documind.common.entity.BaseEntity;
import com.documind.common.enums.UserRole;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a user entity in the system.
 *
 * <p>This class extends {@link BaseEntity} and includes user-specific properties such as
 * authentication details, personal information, and account status flags. It provides methods for
 * managing user account security and status.
 *
 * @author [Your Name]
 * @version 1.0
 * @since [YYYY-MM-DD]
 */
@Entity
@Table(name = "users", schema = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

  /** The unique username for the user. Must be unique across the system and cannot be null. */
  @Column(name = "username", unique = true, nullable = false, length = 50)
  private String username;

  /** The user's email address. Must be unique across the system and cannot be null. */
  @Column(name = "email", unique = true, nullable = false, length = 100)
  private String email;

  /** The hashed password for the user. Cannot be null. */
  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  /** The user's first name. */
  @Column(name = "first_name", length = 50)
  private String firstName;

  /** The user's last name. */
  @Column(name = "last_name", length = 50)
  private String lastName;

  /** The role of the user in the system. Defaults to USER if not specified. */
  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private UserRole role = UserRole.USER;

  /** Flag indicating whether the user account is enabled. Defaults to true. */
  @Column(name = "is_enabled")
  private Boolean isEnabled = true;

  /** Flag indicating whether the user account is expired. Defaults to true. */
  @Column(name = "is_account_non_expired")
  private Boolean isAccountNonExpired = true;

  /** Flag indicating whether the user account is locked. Defaults to true. */
  @Column(name = "is_account_non_locked")
  private Boolean isAccountNonLocked = true;

  /** Flag indicating whether the user's credentials are expired. Defaults to true. */
  @Column(name = "is_credentials_non_expired")
  private Boolean isCredentialsNonExpired = true;

  /** Timestamp of the user's last successful login. */
  @Column(name = "last_login_at")
  private LocalDateTime lastLoginAt;

  /** Count of consecutive failed login attempts. Defaults to 0. */
  @Column(name = "failed_login_attempts")
  private Integer failedLoginAttempts = 0;

  /** Timestamp until which the account is locked due to too many failed login attempts. */
  @Column(name = "locked_until")
  private LocalDateTime lockedUntil;

  /**
   * Returns the full name of the user by combining first and last names. If both names are null,
   * returns the username instead.
   *
   * @return the full name of the user or username if names are not available
   */
  public String getFullName() {
    if (firstName == null && lastName == null) {
      return username;
    }

    return String.format(
            "%s %s", firstName != null ? firstName : "", lastName != null ? lastName : "")
        .trim();
  }

  /**
   * Checks if the account is currently locked.
   *
   * @return true if the account is locked, false otherwise
   */
  public boolean isAccountLocked() {
    return lockedUntil != null && lockedUntil.isAfter(LocalDateTime.now());
  }

  /**
   * Increments the failed login attempts counter. If the counter reaches 5, locks the account for
   * 15 minutes.
   */
  public void incrementFailedAttempts() {
    this.failedLoginAttempts =
        (this.failedLoginAttempts == null ? 0 : this.failedLoginAttempts) + 1;

    if (this.failedLoginAttempts >= 5) {
      this.lockedUntil = LocalDateTime.now().plusMinutes(15);
    }
  }

  /** Resets the failed login attempts counter and unlocks the account. */
  public void resetFailedAttempts() {
    this.failedLoginAttempts = 0;
    this.lockedUntil = null;
  }
}
