package com.documind.user.controller;

import com.documind.common.dto.BaseResponse;
import com.documind.common.dto.PageResponse;
import com.documind.common.enums.UserRole;
import com.documind.user.dto.RegisterRequest;
import com.documind.user.dto.UpdatePasswordRequest;
import com.documind.user.dto.UserDto;
import com.documind.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for user management operations including registration, profile management, and
 * administrative user operations.
 *
 * <p>All endpoints require authentication unless otherwise noted. Administrative endpoints require
 * the ADMIN role.
 *
 * @see UserService
 * @see UserDto
 * @see RegisterRequest
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "User management APIs")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

  private final UserService userService;

  /**
   * Registers a new user account.
   *
   * @param registerRequest the user registration details including username, password, and email
   * @return ResponseEntity containing the created user DTO and success message
   * @throws jakarta.validation.ConstraintViolationException if input validation fails
   */
  @Operation(summary = "Register new user", description = "Create a new user account")
  @PostMapping("/register")
  public ResponseEntity<BaseResponse<UserDto>> registerUser(
      @Valid @RequestBody RegisterRequest registerRequest) {
    log.info("User registration request for username: {}", registerRequest.getUsername());

    UserDto userDto = userService.registerUser(registerRequest);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(userDto, "User registered successfully"));
  }

  /**
   * Retrieves the profile of the currently authenticated user.
   *
   * @param request the HTTP servlet request containing user authentication details
   * @return ResponseEntity containing the user DTO
   */
  @Operation(summary = "Get current user profile", description = "Get authenticated user")
  @GetMapping("/me")
  public ResponseEntity<BaseResponse<UserDto>> getCurrentUser(HttpServletRequest request) {
    UUID userId = getUserIdFromRequest(request);
    log.debug("Getting current user profile for ID: {}", userId);

    UserDto userDto = userService.getUserById(userId);
    return ResponseEntity.ok(BaseResponse.success(userDto));
  }

  /**
   * Updates the profile of the currently authenticated user.
   *
   * @param userDto the updated user details
   * @param request the HTTP servlet request containing user authentication details
   * @return ResponseEntity containing the updated user DTO and success message
   * @throws jakarta.validation.ConstraintViolationException if input validation fails
   */
  @Operation(
      summary = "Update current user profile",
      description = "Update authenticated user's profile")
  @PutMapping("/me")
  public ResponseEntity<BaseResponse<UserDto>> updateCurrentUser(
      @Valid @RequestBody UserDto userDto, HttpServletRequest request) {
    UUID userId = getUserIdFromRequest(request);
    log.info("Updating user profile for ID: {}", userId);

    UserDto updatedUser = userService.updateUser(userId, userDto);

    return ResponseEntity.ok(BaseResponse.success(updatedUser, "Profile updated successfully"));
  }

  /**
   * Updates the password of the currently authenticated user.
   *
   * @param request the password update request containing current and new passwords
   * @param httpRequest the HTTP servlet request containing user authentication details
   * @return ResponseEntity containing success message
   * @throws jakarta.validation.ConstraintViolationException if input validation fails
   * @throws com.documind.common.exception.UnauthorizedException if current password is invalid
   */
  @Operation(summary = "Update password", description = "Update authenticated user's password")
  @PutMapping("/me/password")
  public ResponseEntity<BaseResponse<String>> updatePassword(
      @Valid @RequestBody UpdatePasswordRequest request, HttpServletRequest httpRequest) {
    UUID userId = getUserIdFromRequest(httpRequest);
    log.info("Password update request for user ID: {}", userId);

    userService.updatePassword(userId, request);

    return ResponseEntity.ok(BaseResponse.success("Password updated successfully"));
  }

  /**
   * Retrieves a user by their unique identifier (Admin only).
   *
   * @param userId the UUID of the user to retrieve
   * @return ResponseEntity containing the user DTO
   * @throws com.documind.common.exception.NotFoundException if user is not found
   */
  @Operation(summary = "Get user by ID", description = "Get user information by ID (Admin only)")
  @GetMapping("/{userId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BaseResponse<UserDto>> getUserById(
      @Parameter(description = "User ID") @PathVariable UUID userId) {
    log.debug("Getting user by ID: {}", userId);

    UserDto userDto = userService.getUserById(userId);

    return ResponseEntity.ok(BaseResponse.success(userDto));
  }

  /**
   * Updates a user's information (Admin only).
   *
   * @param userId the UUID of the user to update
   * @param userDto the updated user details
   * @return ResponseEntity containing the updated user DTO and success message
   * @throws jakarta.validation.ConstraintViolationException if input validation fails
   * @throws com.documind.common.exception.NotFoundException if user is not found
   */
  @Operation(summary = "Update user", description = "Update user information (Admin only)")
  @PutMapping("/{userId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BaseResponse<UserDto>> updateUser(
      @Parameter(description = "User ID") @PathVariable UUID userId,
      @Valid @RequestBody UserDto userDto) {
    log.info("Admin updating user: {}", userId);

    UserDto updatedUser = userService.updateUser(userId, userDto);

    return ResponseEntity.ok(BaseResponse.success(updatedUser, "User updated successfully"));
  }

  /**
   * Soft deletes a user (Admin only).
   *
   * @param userId the UUID of the user to delete
   * @return ResponseEntity containing success message
   * @throws com.documind.common.exception.NotFoundException if user is not found
   */
  @Operation(summary = "Delete user", description = "Soft delete user (Admin only)")
  @DeleteMapping("/{userId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BaseResponse<String>> deleteUser(
      @Parameter(description = "User ID") @PathVariable UUID userId) {
    log.info("Admin deleting user: {}", userId);

    userService.deleteUser(userId);

    return ResponseEntity.ok(BaseResponse.success("User deleted successfully"));
  }

  /**
   * Enables a user account (Admin only).
   *
   * @param userId the UUID of the user to enable
   * @return ResponseEntity containing success message
   * @throws com.documind.common.exception.NotFoundException if user is not found
   */
  @Operation(summary = "Enable user", description = "Enable user account (Admin only)")
  @PutMapping("/{userId}/enable")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BaseResponse<String>> enableUser(
      @Parameter(description = "User ID") @PathVariable UUID userId) {
    log.info("Admin enabling user: {}", userId);

    userService.enableUser(userId);

    return ResponseEntity.ok(BaseResponse.success("User enabled successfully"));
  }

  /**
   * Disables a user account (Admin only).
   *
   * @param userId the UUID of the user to disable
   * @return ResponseEntity containing success message
   * @throws com.documind.common.exception.NotFoundException if user is not found
   */
  @Operation(summary = "Disable user", description = "Disable user account (Admin only)")
  @PutMapping("/{userId}/disable")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BaseResponse<String>> disableUser(
      @Parameter(description = "User ID") @PathVariable UUID userId) {
    log.info("Admin disabling user: {}", userId);

    userService.disableUser(userId);

    return ResponseEntity.ok(BaseResponse.success("User disabled successfully"));
  }

  /**
   * Retrieves a paginated list of all users (Admin only).
   *
   * @param page the page number (0-based)
   * @param size the number of items per page
   * @param sortBy the field to sort by
   * @param sortDir the sort direction (asc/desc)
   * @return ResponseEntity containing paginated user DTOs
   */
  @Operation(summary = "Get all users", description = "Get paginated list of users (Admin only)")
  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BaseResponse<PageResponse<UserDto>>> getAllUsers(
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
      @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt")
          String sortBy,
      @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc")
          String sortDir) {

    log.debug(
        "Getting all users - page: {}, size: {}, sortBy: {}, sortDir: {}",
        page,
        size,
        sortBy,
        sortDir);

    Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<UserDto> users = userService.getAllUsers(pageable);
    PageResponse<UserDto> response = PageResponse.of(users);

    return ResponseEntity.ok(BaseResponse.success(response));
  }

  /**
   * Searches users by username, email, or name (Admin only).
   *
   * @param q the search term
   * @param page the page number (0-based)
   * @param size the number of items per page
   * @return ResponseEntity containing paginated search results
   */
  @Operation(
      summary = "Search users",
      description = "Search users by username, email, or name (Admin only)")
  @GetMapping("/search")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BaseResponse<PageResponse<UserDto>>> searchUsers(
      @Parameter(description = "Search term") @RequestParam String q,
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {

    log.debug("Searching users with term: {}", q);

    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<UserDto> users = userService.searchUsers(q, pageable);
    PageResponse<UserDto> response = PageResponse.of(users);

    return ResponseEntity.ok(BaseResponse.success(response));
  }

  /**
   * Retrieves users filtered by role (Admin only).
   *
   * @param role the user role to filter by
   * @param page the page number (0-based)
   * @param size the number of items per page
   * @return ResponseEntity containing paginated user DTOs
   */
  @Operation(summary = "Get users by role", description = "Get users filtered by role (Admin only)")
  @GetMapping("/role/{role}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BaseResponse<PageResponse<UserDto>>> getUsersByRole(
      @Parameter(description = "User role") @PathVariable UserRole role,
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {

    log.debug("Getting users by role: {}", role);

    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<UserDto> users = userService.getUsersByRole(role, pageable);
    PageResponse<UserDto> response = PageResponse.of(users);

    return ResponseEntity.ok(BaseResponse.success(response));
  }

  /**
   * Checks if a username is available for registration.
   *
   * @param username the username to check
   * @return ResponseEntity containing availability status and message
   */
  @Operation(
      summary = "Check username availability",
      description = "Check if username is available")
  @GetMapping("/check/username")
  public ResponseEntity<BaseResponse<Boolean>> checkUsernameAvailability(
      @Parameter(description = "Username to check") @RequestParam String username) {

    boolean exists = userService.existsByUsername(username);
    boolean available = !exists;
    String message = available ? "Username is available" : "Username is already taken";

    return ResponseEntity.ok(BaseResponse.success(available, message));
  }

  /**
   * Checks if an email is available for registration.
   *
   * @param email the email to check
   * @return ResponseEntity containing availability status and message
   */
  @Operation(summary = "Check email availability", description = "Check if email is available")
  @GetMapping("/check/email")
  public ResponseEntity<BaseResponse<Boolean>> checkEmailAvailability(
      @Parameter(description = "Email to check") @RequestParam String email) {

    boolean exists = userService.existsByEmail(email);
    boolean available = !exists;
    String message = available ? "Email is available" : "Email is already taken";

    return ResponseEntity.ok(BaseResponse.success(available, message));
  }

  /**
   * Retrieves user count statistics (Admin only).
   *
   * @return ResponseEntity containing user statistics
   */
  @Operation(
      summary = "Get user statistics",
      description = "Get user count statistics (Admin only)")
  @GetMapping("/stats")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BaseResponse<UserStats>> getUserStats() {
    log.debug("Getting user statistics");

    long totalUsers = userService.countActiveUsers();
    long adminUsers = userService.countUsersByRole(UserRole.ADMIN);
    long regularUsers = userService.countUsersByRole(UserRole.USER);
    long viewerUsers = userService.countUsersByRole(UserRole.VIEWER);

    UserStats stats = new UserStats(totalUsers, adminUsers, regularUsers, viewerUsers);

    return ResponseEntity.ok(BaseResponse.success(stats));
  }

  /**
   * Extracts the user ID from the HTTP request attributes.
   *
   * @param request the HTTP servlet request
   * @return the user ID as UUID
   */
  private UUID getUserIdFromRequest(HttpServletRequest request) {
    return (UUID) request.getAttribute("userId");
  }

  /**
   * Immutable record representing user statistics.
   *
   * @param totalUsers total count of active users
   * @param adminUsers count of users with ADMIN role
   * @param regularUsers count of users with USER role
   * @param viewerUsers count of users with VIEWER role
   */
  public record UserStats(long totalUsers, long adminUsers, long regularUsers, long viewerUsers) {}
}
