package com.documind.user.service.impl;

import com.documind.common.enums.UserRole;
import com.documind.common.exception.ResourceNotFoundException;
import com.documind.common.exception.ValidationException;
import com.documind.user.dto.RegisterRequest;
import com.documind.user.dto.UpdatePasswordRequest;
import com.documind.user.dto.UserDto;
import com.documind.user.entity.User;
import com.documind.user.mapper.UserMapper;
import com.documind.user.repository.UserRepository;
import com.documind.user.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link UserService} for managing user operations.
 *
 * <p>This service handles all user-related business logic including registration, retrieval,
 * updates, and deletions. It includes caching support for frequently accessed user data and proper
 * transaction management.
 *
 * @author Carlos Salguero
 * @version 1.0
 * @since 2025-08-06
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  /**
   * {@inheritDoc}
   *
   * <p>Registers a new user after validating username and email uniqueness. Encrypts the password
   * before storage and sets default role to USER.
   *
   * @throws ValidationException if username or email already exists
   */
  @Override
  @Transactional
  public UserDto registerUser(RegisterRequest registerRequest) {
    log.info("Registering new user with username: {}", registerRequest.getUsername());

    if (existsByUsername(registerRequest.getUsername())) {
      throw new ValidationException("username", "Username already exists");
    }

    if (existsByEmail(registerRequest.getEmail())) {
      throw new ValidationException("email", "Email already exists");
    }

    User user = userMapper.toEntity(registerRequest);
    user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
    user.setRole(UserRole.USER);

    User savedUser = userRepository.save(user);
    log.info("User registered successfully with ID: {}", savedUser.getId());

    return userMapper.toDto(savedUser);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Retrieves user by ID with caching support. Subsequent calls for the same user ID will be
   * served from cache when available.
   *
   * @throws ResourceNotFoundException if user is not found
   */
  @Override
  @Cacheable(value = "users", key = "#userId")
  public UserDto getUserById(UUID userId) {
    log.debug("Getting user by ID: {}", userId);
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

    return userMapper.toDto(user);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Retrieves user by username with caching support.
   *
   * @throws ResourceNotFoundException if user is not found
   */
  @Override
  @Cacheable(value = "users", key = "#username")
  public UserDto getUserByUsername(String username) {
    log.debug("Getting user by username: {}", username);
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username: " + username));

    return userMapper.toDto(user);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Retrieves user by email with caching support.
   *
   * @throws ResourceNotFoundException if user is not found
   */
  @Override
  @Cacheable(value = "users", key = "#email")
  public UserDto getUserByEmail(String email) {
    log.debug("Getting user by email: {}", email);
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email: " + email));

    return userMapper.toDto(user);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Updates user information while ignoring immutable fields like username, email, and password.
   * Clears the user cache after update.
   *
   * @throws ResourceNotFoundException if user is not found
   */
  @Override
  @Transactional
  @CacheEvict(value = "users", key = "#userId")
  public UserDto updateUser(UUID userId, UserDto userDto) {
    log.info("Updating user with ID: {}", userId);

    User existingUser =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

    userMapper.updateUserFromDto(userDto, existingUser);
    User updatedUser = userRepository.save(existingUser);
    log.info("User updated successfully: {}", userId);

    return userMapper.toDto(updatedUser);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Updates user password after validating current password and confirmation. Clears the user
   * cache after update.
   *
   * @throws ResourceNotFoundException if user is not found
   * @throws ValidationException if current password is incorrect or confirmation doesn't match
   */
  @Override
  @Transactional
  @CacheEvict(value = "users", key = "#userId")
  public void updatePassword(UUID userId, UpdatePasswordRequest request) {
    log.info("Updating password for user: {}", userId);

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
      throw new ValidationException("currentPassword", "Current password is incorrect");
    }

    if (!request.getNewPassword().equals(request.getConfirmPassword())) {
      throw new ValidationException("confirmPassword", "Password confirmation does not match");
    }

    user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
    user.setIsCredentialsNonExpired(true);
    userRepository.save(user);
    log.info("Password updated successfully for user: {}", userId);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Performs a soft delete of the user by marking them as deleted and disabled. Clears the user
   * cache after deletion.
   *
   * @throws ResourceNotFoundException if user is not found
   */
  @Override
  @Transactional
  @CacheEvict(value = "users", key = "#userId")
  public void deleteUser(UUID userId) {
    log.info("Soft deleting user: {}", userId);

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

    user.setIsDeleted(true);
    user.setIsEnabled(false);
    userRepository.save(user);
    log.info("User soft deleted: {}", userId);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Enables a user account and clears the user cache.
   *
   * @throws ResourceNotFoundException if user is not found
   */
  @Override
  @Transactional
  @CacheEvict(value = "users", key = "#userId")
  public void enableUser(UUID userId) {
    log.info("Enabling user: {}", userId);
    updateUserStatus(userId, true);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Disables a user account and clears the user cache.
   *
   * @throws ResourceNotFoundException if user is not found
   */
  @Override
  @Transactional
  @CacheEvict(value = "users", key = "#userId")
  public void disableUser(UUID userId) {
    log.info("Disabling user: {}", userId);
    updateUserStatus(userId, false);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Retrieves all active users (enabled and not deleted) with pagination support.
   */
  @Override
  public Page<UserDto> getAllUsers(Pageable pageable) {
    log.debug("Getting all users with pagination: {}", pageable);
    return userRepository.findAllActiveUsers(pageable).map(userMapper::toDto);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Retrieves users by role with pagination support, excluding deleted users.
   */
  @Override
  public Page<UserDto> getUsersByRole(UserRole role, Pageable pageable) {
    log.debug("Getting users by role: {} with pagination: {}", role, pageable);
    return userRepository.findByRole(role, pageable).map(userMapper::toDto);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Searches users by username, email, first name, or last name with pagination support. The
   * search is case-insensitive and excludes deleted users.
   */
  @Override
  public Page<UserDto> searchUsers(String searchTerm, Pageable pageable) {
    log.debug("Searching users with term: {} and pagination: {}", searchTerm, pageable);
    return userRepository.searchUsers(searchTerm, pageable).map(userMapper::toDto);
  }

  /** {@inheritDoc} */
  @Override
  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  /** {@inheritDoc} */
  @Override
  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Counts all active users (not deleted) in the system.
   */
  @Override
  public long countActiveUsers() {
    return userRepository.countActiveUsers();
  }

  /**
   * {@inheritDoc}
   *
   * <p>Counts users by role, excluding deleted users.
   */
  @Override
  public long countUsersByRole(UserRole role) {
    return userRepository.countUsersByRole(role);
  }

  /**
   * Updates the enabled/disabled status of a user.
   *
   * @param userId the ID of the user to update
   * @param enabled true to enable, false to disable
   * @throws ResourceNotFoundException if user is not found
   */
  private void updateUserStatus(UUID userId, boolean enabled) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

    user.setIsEnabled(enabled);
    userRepository.save(user);
    log.info("User {} status updated to: {}", userId, enabled ? "enabled" : "disabled");
  }
}
