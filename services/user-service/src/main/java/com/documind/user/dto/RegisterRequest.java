package com.documind.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** DTO for user registration requests. */
@Data
public class RegisterRequest {

  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
  private String username;

  @NotBlank(message = "Email is required")
  @Email(message = "Email must be valid")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
  private String password;

  @Size(max = 50, message = "First name cannot exceed 50 characters")
  private String firstName;

  @Size(max = 50, message = "Last name cannot exceed 50 characters")
  private String lastName;
}
