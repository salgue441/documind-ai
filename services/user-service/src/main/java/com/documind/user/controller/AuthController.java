package com.documind.user.controller;

import com.documind.common.dto.BaseResponse;
import com.documind.user.dto.LoginRequest;
import com.documind.user.dto.LoginResponse;
import com.documind.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** REST controller for authentication operations. */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "User login", description = "Authenticate user and return JWT tokens")
  @PostMapping("/login")
  public ResponseEntity<BaseResponse<LoginResponse>> login(
      @Valid @RequestBody LoginRequest loginRequest) {
    log.info("Login request received for: {}", loginRequest.getUsernameOrEmail());

    LoginResponse loginResponse = authService.login(loginRequest);

    return ResponseEntity.ok(BaseResponse.success(loginResponse, "Login successful"));
  }

  @Operation(
      summary = "Refresh token",
      description = "Refresh JWT access token using refresh token")
  @PostMapping("/refresh")
  public ResponseEntity<BaseResponse<LoginResponse>> refreshToken(
      @RequestBody String refreshToken) {
    log.info("Token refresh request received");

    LoginResponse loginResponse = authService.refreshToken(refreshToken);

    return ResponseEntity.ok(BaseResponse.success(loginResponse, "Token refreshed successfully"));
  }

  @Operation(summary = "User logout", description = "Logout user and invalidate tokens")
  @PostMapping("/logout")
  public ResponseEntity<BaseResponse<String>> logout(HttpServletRequest request) {
    String token = getTokenFromRequest(request);

    if (token != null) {
      authService.logout(token);
      log.info("User logged out successfully");
    }

    return ResponseEntity.ok(BaseResponse.success("Logged out successfully"));
  }

  @Operation(summary = "Validate token", description = "Validate JWT token")
  @PostMapping("/validate")
  public ResponseEntity<BaseResponse<Boolean>> validateToken(HttpServletRequest request) {
    String token = getTokenFromRequest(request);

    if (token == null) {
      return ResponseEntity.ok(BaseResponse.success(false, "No token provided"));
    }

    boolean isValid = authService.validateToken(token);
    String message = isValid ? "Token is valid" : "Token is invalid";

    return ResponseEntity.ok(BaseResponse.success(isValid, message));
  }

  private String getTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
