package com.documind.user.service.impl;

import com.documind.common.exception.UnauthorizedException;
import com.documind.common.security.JwtUtil;
import com.documind.user.dto.LoginRequest;
import com.documind.user.dto.LoginResponse;
import com.documind.user.entity.User;
import com.documind.user.mapper.UserMapper;
import com.documind.user.repository.UserRepository;
import com.documind.user.service.AuthService;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link AuthService} for authentication and authorization operations.
 *
 * <p>Handles user authentication, JWT token generation/validation, and account security features
 * including login attempt tracking and account locking. Integrates with Redis for token management
 * and caching.
 *
 * @author Carlos Salguero
 * @version 1.0
 * @since 2025-08-06
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final RedisTemplate<String, String> redisTemplate;

  @Value("${jwt.expiration:86400000}") // Default 24 hours
  private Long jwtExpiration;

  @Value("${jwt.refresh-expiration:604800000}") // Default 7 days
  private Long refreshExpiration;

  /**
   * {@inheritDoc}
   *
   * <p>Authenticates user credentials and generates JWT tokens upon successful authentication.
   * Handles account locking after multiple failed attempts and validates account status.
   *
   * @throws UnauthorizedException if authentication fails or account is locked/disabled
   */
  @Override
  @Transactional
  public LoginResponse login(LoginRequest loginRequest) {
    log.info("Login attempt for: {}", loginRequest.getUsernameOrEmail());

    User user =
        userRepository
            .findByUsernameOrEmail(
                loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail())
            .orElseThrow(() -> new UnauthorizedException("Invalid username/email or password"));

    validateAccountStatus(user);
    validateCredentials(loginRequest, user);

    return createLoginResponse(user);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Generates new access token using a valid refresh token. Invalidates the old refresh token
   * and issues new tokens. Maintains single active refresh token per user.
   *
   * @throws UnauthorizedException if refresh token is invalid or expired
   */
  @Override
  @Transactional
  public LoginResponse refreshToken(String refreshToken) {
    log.debug("Refreshing token");

    String username = jwtUtil.getUsernameFromToken(refreshToken);
    validateRefreshToken(refreshToken, username);

    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UnauthorizedException("User not found"));

    return createRefreshResponse(user);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Invalidates the provided token by adding it to the blacklist and removes associated tokens
   * from Redis cache. Designed to be idempotent.
   */
  @Override
  public void logout(String token) {
    try {
      String username = jwtUtil.getUsernameFromToken(token);
      invalidateTokens(token, username);
      log.info("User {} logged out successfully", username);
    } catch (Exception e) {
      log.error("Error during logout: {}", e.getMessage());
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p>Validates token signature, expiration, and checks against the blacklist.
   *
   * @return true if token is valid and not blacklisted, false otherwise
   */
  @Override
  public boolean validateToken(String token) {
    try {
      if (isTokenBlacklisted(token)) {
        return false;
      }

      String username = jwtUtil.getUsernameFromToken(token);
      return jwtUtil.validateToken(token, username);
    } catch (Exception e) {
      log.debug("Token validation failed: {}", e.getMessage());
      return false;
    }
  }

  /**
   * {@inheritDoc}
   *
   * <p>Locks user account for security purposes. Account will remain locked until explicitly
   * unlocked or lock duration expires.
   *
   * @throws ResourceNotFoundException if user is not found
   */
  @Override
  @Transactional
  public void lockAccount(String usernameOrEmail) {
    log.info("Locking account: {}", usernameOrEmail);
    User user = getUserByUsernameOrEmail(usernameOrEmail);

    user.setLockedUntil(LocalDateTime.now().plusMinutes(15));
    userRepository.save(user);
    log.info("Account locked: {}", usernameOrEmail);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Unlocks user account and resets failed login attempts counter.
   *
   * @throws ResourceNotFoundException if user is not found
   */
  @Override
  @Transactional
  public void unlockAccount(String usernameOrEmail) {
    log.info("Unlocking account: {}", usernameOrEmail);
    User user = getUserByUsernameOrEmail(usernameOrEmail);

    user.resetFailedAttempts();
    userRepository.save(user);
    log.info("Account unlocked: {}", usernameOrEmail);
  }

  /**
   * Validates user account status before allowing login.
   *
   * @param user the user attempting to login
   * @throws UnauthorizedException if account is locked or disabled
   */
  private void validateAccountStatus(User user) {
    if (user.isAccountLocked()) {
      log.warn("Login attempt for locked account: {}", user.getUsername());
      throw new UnauthorizedException(
          "Account is temporarily locked due to multiple failed login attempts");
    }

    if (!user.getIsEnabled()) {
      log.warn("Login attempt for disabled account: {}", user.getUsername());
      throw new UnauthorizedException("Account is disabled");
    }
  }

  /**
   * Validates user credentials and handles failed attempts.
   *
   * @param loginRequest the login credentials
   * @param user the user to validate against
   * @throws UnauthorizedException if credentials are invalid
   */
  private void validateCredentials(LoginRequest loginRequest, User user) {
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
      handleFailedLogin(user);
      throw new UnauthorizedException("Invalid username/email or password");
    }
  }

  /**
   * Handles failed login attempt by incrementing counter and locking account if necessary.
   *
   * @param user the user who failed authentication
   */
  private void handleFailedLogin(User user) {
    user.incrementFailedAttempts();
    userRepository.save(user);
    log.warn(
        "Failed login attempt {} for user: {}", user.getFailedLoginAttempts(), user.getUsername());
  }

  /**
   * Creates login response with new tokens upon successful authentication.
   *
   * @param user the authenticated user
   * @return LoginResponse containing tokens and user details
   */
  private LoginResponse createLoginResponse(User user) {
    user.resetFailedAttempts();
    user.setLastLoginAt(LocalDateTime.now());
    userRepository.save(user);

    String accessToken =
        jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole().name());
    String refreshToken = generateRefreshToken(user);
    cacheTokens(user.getId().toString(), accessToken, refreshToken);

    log.info("User {} logged in successfully", user.getUsername());
    return new LoginResponse(
        accessToken, refreshToken, jwtExpiration / 1000, userMapper.toDto(user));
  }

  /**
   * Validates refresh token against Redis cache.
   *
   * @param refreshToken the token to validate
   * @param username the username associated with the token
   * @throws UnauthorizedException if token is invalid
   */
  private void validateRefreshToken(String refreshToken, String username) {
    String cachedToken = redisTemplate.opsForValue().get("refresh_token:" + username);
    if (cachedToken == null || !cachedToken.equals(refreshToken)) {
      throw new UnauthorizedException("Invalid refresh token");
    }
  }

  /**
   * Creates new token response during refresh operation.
   *
   * @param user the user requesting refresh
   * @return LoginResponse with new tokens
   */
  private LoginResponse createRefreshResponse(User user) {
    String newAccessToken =
        jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole().name());
    String newRefreshToken = generateRefreshToken(user);
    cacheTokens(user.getId().toString(), newAccessToken, newRefreshToken);

    log.debug("Token refreshed successfully for user: {}", user.getUsername());
    return new LoginResponse(
        newAccessToken, newRefreshToken, jwtExpiration / 1000, userMapper.toDto(user));
  }

  /**
   * Invalidates tokens during logout operation.
   *
   * @param token the access token to invalidate
   * @param username the username associated with the tokens
   */
  private void invalidateTokens(String token, String username) {
    redisTemplate.delete("access_token:" + username);
    redisTemplate.delete("refresh_token:" + username);
    redisTemplate
        .opsForValue()
        .set("blacklist:" + token, "true", jwtExpiration, TimeUnit.MILLISECONDS);
  }

  /**
   * Checks if token exists in blacklist.
   *
   * @param token the token to check
   * @return true if token is blacklisted, false otherwise
   */
  private boolean isTokenBlacklisted(String token) {
    return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token));
  }

  /**
   * Retrieves user by username or email.
   *
   * @param usernameOrEmail the username or email to search for
   * @return the found user
   * @throws ResourceNotFoundException if user is not found
   */
  private User getUserByUsernameOrEmail(String usernameOrEmail) {
    return userRepository
        .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
        .orElseThrow(() -> new ResourceNotFoundException("User", usernameOrEmail));
  }

  /**
   * Generates a new refresh token.
   *
   * @param user the user to generate token for
   * @return the generated refresh token
   */
  private String generateRefreshToken(User user) {
    return jwtUtil.generateToken(user.getUsername(), user.getId(), "REFRESH");
  }

  /**
   * Caches tokens in Redis with appropriate expiration times.
   *
   * @param userId the user ID for cache key
   * @param accessToken the access token to cache
   * @param refreshToken the refresh token to cache
   */
  private void cacheTokens(String userId, String accessToken, String refreshToken) {
    redisTemplate
        .opsForValue()
        .set("access_token:" + userId, accessToken, jwtExpiration, TimeUnit.MILLISECONDS);

    redisTemplate
        .opsForValue()
        .set("refresh_token:" + userId, refreshToken, refreshExpiration, TimeUnit.MILLISECONDS);
  }
}
