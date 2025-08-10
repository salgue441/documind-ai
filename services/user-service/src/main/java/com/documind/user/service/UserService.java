package com.documind.user.service;

import com.documind.common.enums.UserRole;
import com.documind.user.dto.RegisterRequest;
import com.documind.user.dto.UpdatePasswordRequest;
import com.documind.user.dto.UserDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for user management operations.
 *
 * <p>Provides methods for user registration, retrieval, update, deletion, and various query
 * operations with pagination support.
 *
 * @author Carlos Salguero
 * @version 1.0
 * @since 2025-08-06
 */
public interface UserService {
  /**
   * Registers a new user in the system.
   *
   * @param registerRequest the registration request containing user details
   * @return the registered user's DTO
   */
  UserDto registerUser(RegisterRequest registerRequest);

  /**
   * Retrieves a user by their unique identifier.
   *
   * @param userId the user's UUID
   * @return the user DTO if found
   */
  UserDto getUserById(UUID userId);

  /**
   * Retrieves a user by their username.
   *
   * @param username the username to search for
   * @return the user DTO if found
   */
  UserDto getUserByUsername(String username);

  /**
   * Retrieves a user by their email address.
   *
   * @param email the email address to search for
   * @return the user DTO if found
   */
  UserDto getUserByEmail(String email);

  /**
   * Updates a user's information.
   *
   * @param userId the ID of the user to update
   * @param userDto the updated user data
   * @return the updated user DTO
   */
  UserDto updateUser(UUID userId, UserDto userDto);

  /**
   * Updates a user's password after validating the current password.
   *
   * @param userId the ID of the user
   * @param request the password update request containing current and new passwords
   */
  void updatePassword(UUID userId, UpdatePasswordRequest request);

  /**
   * Deletes a user (soft delete if implemented).
   *
   * @param userId the ID of the user to delete
   */
  void deleteUser(UUID userId);

  /**
   * Enables a user account.
   *
   * @param userId the ID of the user to enable
   */
  void enableUser(UUID userId);

  /**
   * Disables a user account.
   *
   * @param userId the ID of the user to disable
   */
  void disableUser(UUID userId);

  /**
   * Retrieves all users with pagination.
   *
   * @param pageable pagination information
   * @return a page of user DTOs
   */
  Page<UserDto> getAllUsers(Pageable pageable);

  /**
   * Retrieves users by their role with pagination.
   *
   * @param role the role to filter by
   * @param pageable pagination information
   * @return a page of user DTOs with the specified role
   */
  Page<UserDto> getUsersByRole(UserRole role, Pageable pageable);

  /**
   * Searches users by various fields with pagination.
   *
   * @param searchTerm the term to search for
   * @param pageable pagination information
   * @return a page of matching user DTOs
   */
  Page<UserDto> searchUsers(String searchTerm, Pageable pageable);

  /**
   * Checks if a username exists in the system.
   *
   * @param username the username to check
   * @return true if the username exists, false otherwise
   */
  boolean existsByUsername(String username);

  /**
   * Checks if an email address exists in the system.
   *
   * @param email the email address to check
   * @return true if the email exists, false otherwise
   */
  boolean existsByEmail(String email);

  /**
   * Counts all active users in the system.
   *
   * @return the number of active users
   */
  long countActiveUsers();

  /**
   * Counts users by their role.
   *
   * @param role the role to filter by
   * @return the number of users with the specified role
   */
  long countUsersByRole(UserRole role);
}
