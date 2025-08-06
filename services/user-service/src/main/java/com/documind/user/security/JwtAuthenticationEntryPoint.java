package com.documind.user.security;

import com.documind.common.dto.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Handles authentication failures by returning a standardized error response.
 *
 * <p>This component intercepts unauthorized requests and returns a consistent JSON error response
 * with HTTP 401 status. It logs authentication failures and prevents default Spring Security
 * redirect behavior.
 *
 * @author Carlos Salguero
 * @version 1.0
 * @since 2025-08-06
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  /**
   * Invoked when an unauthenticated user attempts to access a secured resource.
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @param authException the authentication exception that occurred
   * @throws IOException if there's an error writing to the response
   */
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {

    log.error("Unauthorized access attempt: {}", authException.getMessage());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    BaseResponse<Object> errorResponse =
        BaseResponse.error("Unauthorized access - Authentication required");

    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}
