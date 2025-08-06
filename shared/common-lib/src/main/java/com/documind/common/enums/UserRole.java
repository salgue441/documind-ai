package com.documind.common.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * Enumeration for user roles and permissions in the system.
 *
 * <p>Implements Spring Security's {@link GrantedAuthority} for integration with Spring's security
 * framework. Includes permission-checking methods.
 */
public enum UserRole implements GrantedAuthority {
  /** System administrator with full access privileges. */
  ADMIN("ROLE_ADMIN", "System administrator with full access"),

  /** Regular user with standard modification privileges. */
  USER("ROLE_USER", "Regular user with standard access"),

  /** Read-only user with viewing privileges only. */
  VIEWER("ROLE_VIEWER", "Read-only access to documents"),

  /** External system with API access privileges. */
  API_CLIENT("ROLE_API_CLIENT", "External API client access");

  private final String authority;
  private final String description;

  UserRole(String authority, String description) {
    this.authority = authority;
    this.description = description;
  }

  /**
   * Gets the authority string for Spring Security.
   *
   * @return role authority string (e.g., "ROLE_ADMIN")
   */
  @Override
  public String getAuthority() {
    return authority;
  }

  /**
   * Gets the human-readable description of this role.
   *
   * @return role description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Checks if this role has administrator privileges.
   *
   * @return true if role is ADMIN
   */
  public boolean isAdmin() {
    return this == ADMIN;
  }

  /**
   * Checks if this role has document modification privileges.
   *
   * @return true if role is ADMIN or USER
   */
  public boolean canModify() {
    return this == ADMIN || this == USER;
  }

  /**
   * Checks if this role has document viewing privileges.
   *
   * @return true for all roles (all roles can view)
   */
  public boolean canView() {
    return true; // All roles can view
  }
}
