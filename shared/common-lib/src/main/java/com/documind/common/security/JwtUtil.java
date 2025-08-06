package com.documind.common.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class for JSON Web Token (JWT) operations.
 *
 * <p>Provides methods for:
 *
 * <ul>
 *   <li>Token generation and signing
 *   <li>Token validation and parsing
 *   <li>Claim extraction
 *   <li>Token refresh
 * </ul>
 *
 * Uses HMAC-SHA512 algorithm for signing and verifies tokens against a secret key.
 *
 * @see io.jsonwebtoken.Jwts
 * @see io.jsonwebtoken.Claims
 */
@Slf4j
@Component
public class JwtUtil {

  @Value("${jwt.secret:your-super-secret-jwt-key-change-in-production}")
  private String jwtSecret;

  @Value("${jwt.expiration:86400000}") // 24 hours in milliseconds
  private Long jwtExpiration;

  /**
   * Creates the signing key from the configured secret.
   *
   * @return SecretKey for JWT signing/verification
   */
  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  /**
   * Generates a JWT token with user details.
   *
   * @param username the subject of the token
   * @param userId unique user identifier
   * @param role user's role/authority
   * @return signed JWT token
   */
  public String generateToken(String username, UUID userId, String role) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", userId.toString());
    claims.put("role", role);
    return createToken(claims, username);
  }

  /**
   * Creates a signed JWT token with specified claims.
   *
   * @param claims additional token claims
   * @param subject the token subject (typically username)
   * @return signed JWT token
   */
  private String createToken(Map<String, Object> claims, String subject) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpiration);

    return Jwts.builder()
        .claims(claims)
        .subject(subject)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey(), SignatureAlgorithm.HS512)
        .compact();
  }

  /**
   * Extracts the username from a JWT token.
   *
   * @param token JWT token
   * @return username/subject from token
   */
  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  /**
   * Extracts the user ID from a JWT token.
   *
   * @param token JWT token
   * @return UUID from token claims
   * @throws IllegalArgumentException if userId claim is missing or invalid
   */
  public UUID getUserIdFromToken(String token) {
    String userIdString = getClaimFromToken(token, claims -> claims.get("userId", String.class));
    return UUID.fromString(userIdString);
  }

  /**
   * Extracts the user role from a JWT token.
   *
   * @param token JWT token
   * @return role from token claims
   */
  public String getRoleFromToken(String token) {
    return getClaimFromToken(token, claims -> claims.get("role", String.class));
  }

  /**
   * Extracts the expiration date from a JWT token.
   *
   * @param token JWT token
   * @return expiration date from token
   */
  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  /**
   * Generic method to extract a claim from a token.
   *
   * @param <T> type of the claim
   * @param token JWT token
   * @param claimsResolver function to extract the claim
   * @return the claim value
   */
  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Parses and validates a JWT token, returning all claims.
   *
   * @param token JWT token to parse
   * @return all token claims
   * @throws ExpiredJwtException if token is expired
   * @throws UnsupportedJwtException if token is unsupported
   * @throws MalformedJwtException if token is malformed
   * @throws SecurityException if signature verification fails
   * @throws IllegalArgumentException if token is empty or null
   */
  private Claims getAllClaimsFromToken(String token) {
    try {
      return Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (ExpiredJwtException e) {
      log.warn("JWT token is expired: {}", e.getMessage());
      throw e;
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
      throw e;
    } catch (MalformedJwtException e) {
      log.error("JWT token is malformed: {}", e.getMessage());
      throw e;
    } catch (SecurityException e) {
      log.error("Invalid JWT signature: {}", e.getMessage());
      throw e;
    } catch (IllegalArgumentException e) {
      log.error("JWT token compact of handler are invalid: {}", e.getMessage());
      throw e;
    }
  }

  /**
   * Checks if a token is expired.
   *
   * @param token JWT token to check
   * @return true if token is expired, false otherwise
   */
  public Boolean isTokenExpired(String token) {
    try {
      final Date expiration = getExpirationDateFromToken(token);
      return expiration.before(new Date());
    } catch (ExpiredJwtException e) {
      return true;
    }
  }

  /**
   * Validates a token against a username.
   *
   * @param token JWT token to validate
   * @param username username to validate against
   * @return true if token is valid for the given username
   */
  public Boolean validateToken(String token, String username) {
    try {
      final String tokenUsername = getUsernameFromToken(token);
      return (username.equals(tokenUsername) && !isTokenExpired(token));
    } catch (Exception e) {
      log.error("JWT validation failed: {}", e.getMessage());
      return false;
    }
  }

  /**
   * Refreshes a token by extending its expiration.
   *
   * @param token original JWT token
   * @return new token with extended expiration
   * @throws RuntimeException if refresh fails
   */
  public String refreshToken(String token) {
    try {
      final Claims claims = getAllClaimsFromToken(token);

      return Jwts.builder()
          .claims(claims)
          .subject(claims.getSubject())
          .issuedAt(new Date())
          .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
          .signWith(getSigningKey(), SignatureAlgorithm.HS512)
          .compact();
    } catch (Exception e) {
      log.error("Failed to refresh token: {}", e.getMessage());
      throw new RuntimeException("Token refresh failed", e);
    }
  }
}
