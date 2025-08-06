package com.documind.user.repository;

import com.documind.common.enums.UserRole;
import com.documind.user.entity.User;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link User} entities.
 *
 * <p>Provides CRUD operations and custom queries for user management, including authentication,
 * search, and user status operations. Extends {@link JpaRepository} for basic JPA operations.
 *
 * @author Carlos Salguero
 * @version 1.0
 * @since 2025-08-06
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  /**
   * Finds a user by their username.
   *
   * @param username the username to search for
   * @return an {@link Optional} containing the user if found
   */
  Optional<User> findByUsername(String username);

  /**
   * Finds a user by their email address.
   *
   * @param email the email address to search for
   * @return an {@link Optional} containing the user if found
   */
  Optional<User> findByEmail(String email);

  /**
   * Finds a user by either username or email address.
   *
   * @param username the username to search for
   * @param email the email address to search for
   * @return an {@link Optional} containing the user if found by either credential
   */
  Optional<User> findByUsernameOrEmail(String username, String email);

  /**
   * Checks if a username already exists in the system.
   *
   * @param username the username to check
   * @return true if the username exists, false otherwise
   */
  boolean existsByUsername(String username);

  /**
   * Checks if an email address already exists in the system.
   *
   * @param email the email address to check
   * @return true if the email exists, false otherwise
   */
  boolean existsByEmail(String email);

  /**
   * Finds all active (enabled and not deleted) users with pagination.
   *
   * @param pageable pagination information
   * @return a {@link Page} of active users
   */
  @Query("SELECT u FROM User u WHERE u.isEnabled = true AND u.isDeleted = false")
  Page<User> findAllActiveUsers(Pageable pageable);

  /**
   * Finds users by their role with pagination, excluding deleted users.
   *
   * @param role the user role to filter by
   * @param pageable pagination information
   * @return a {@link Page} of users with the specified role
   */
  @Query("SELECT u FROM User u WHERE u.role = :role AND u.isDeleted = false")
  Page<User> findByRole(@Param("role") UserRole role, Pageable pageable);

  /**
   * Searches users by various fields with pagination, excluding deleted users.
   *
   * <p>Searches username, email, first name, and last name case-insensitively.
   *
   * @param searchTerm the term to search for
   * @param pageable pagination information
   * @return a {@link Page} of matching users
   */
  @Query(
      "SELECT u FROM User u WHERE "
          + "(LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
          + "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
          + "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
          + "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) "
          + "AND u.isDeleted = false")
  Page<User> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);

  /**
   * Updates the last login time for a specific user.
   *
   * @param userId the ID of the user to update
   * @param loginTime the new login timestamp
   */
  @Modifying
  @Query("UPDATE User u SET u.lastLoginAt = :loginTime WHERE u.id = :userId")
  void updateLastLoginTime(
      @Param("userId") UUID userId, @Param("loginTime") LocalDateTime loginTime);

  /**
   * Updates the failed login attempts counter for a specific user.
   *
   * @param userId the ID of the user to update
   * @param attempts the new failed attempts count
   */
  @Modifying
  @Query("UPDATE User u SET u.failedLoginAttempts = :attempts WHERE u.id = :userId")
  void updateFailedLoginAttempts(@Param("userId") UUID userId, @Param("attempts") Integer attempts);

  /**
   * Counts all active (not deleted) users in the system.
   *
   * @return the count of active users
   */
  @Query("SELECT COUNT(u) FROM User u WHERE u.isDeleted = false")
  long countActiveUsers();

  /**
   * Counts users by their role, excluding deleted users.
   *
   * @param role the role to filter by
   * @return the count of users with the specified role
   */
  @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.isDeleted = false")
  long countUsersByRole(@Param("role") UserRole role);
}
