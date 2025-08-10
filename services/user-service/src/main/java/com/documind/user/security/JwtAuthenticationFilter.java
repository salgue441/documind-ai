package com.documind.user.security;

import com.documind.common.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Processes JWT tokens in incoming requests and establishes security context.
 *
 * <p>This filter:
 *
 * <ul>
 *   <li>Extracts JWT tokens from Authorization header
 *   <li>Validates tokens using JwtUtil directly
 *   <li>Sets up Spring Security context for authenticated users
 *   <li>Adds user attributes to the request for downstream use
 * </ul>
 *
 * @author Carlos Salguero
 * @version 1.0
 * @since 2025-08-06
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  /**
   * Processes each request to extract and validate JWT tokens.
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @param filterChain the filter chain to continue processing
   * @throws ServletException if a servlet error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    String token = getTokenFromRequest(request);

    if (token != null && isValidToken(token)) {
      try {
        authenticateRequest(request, token);
      } catch (Exception e) {
        log.error("Authentication error: {}", e.getMessage());
        SecurityContextHolder.clearContext();
      }
    }

    filterChain.doFilter(request, response);
  }

  /**
   * Extracts JWT token from Authorization header.
   *
   * @param request the HTTP request
   * @return the extracted token or null if not found
   */
  private String getTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    return StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")
        ? bearerToken.substring(7)
        : null;
  }

  /**
   * Validates the JWT token using JwtUtil.
   *
   * @param token the token to validate
   * @return true if token is valid, false otherwise
   */
  private boolean isValidToken(String token) {
    try {
      String username = jwtUtil.getUsernameFromToken(token);
      return jwtUtil.validateToken(token, username);
    } catch (Exception e) {
      log.debug("Token validation failed: {}", e.getMessage());
      return false;
    }
  }

  /**
   * Sets up security context and request attributes for authenticated users.
   *
   * @param request the HTTP request
   * @param token the validated JWT token
   */
  private void authenticateRequest(HttpServletRequest request, String token) {
    String username = jwtUtil.getUsernameFromToken(token);
    String role = jwtUtil.getRoleFromToken(token);
    UUID userId = jwtUtil.getUserIdFromToken(token);

    var authorities = List.of(new SimpleGrantedAuthority(role));
    var authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    request.setAttribute("userId", userId);
    request.setAttribute("username", username);
    request.setAttribute("role", role);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    log.debug("Authenticated user: {}", username);
  }
}
