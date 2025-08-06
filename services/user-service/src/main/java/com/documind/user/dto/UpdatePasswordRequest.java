package com.documind.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object for password update requests.
 *
 * <p>Contains the current password, new password, and confirmation of the new password with
 * appropriate validation constraints.
 *
 * @author Carlos Salguero
 * @version 1.0
 * @since 2025-08-06
 */
@Data
public class UpdatePasswordRequest {

  /** The user's current password for verification. Cannot be blank. */
  @NotBlank(message = "Current password is required")
  private String currentPassword;

  /** The new desired password. Must be between 8 and 100 characters. */
  @NotBlank(message = "New password is required")
  @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
  private String newPassword;

  /** Confirmation of the new password. Should match the new password field. Cannot be blank. */
  @NotBlank(message = "Password confirmation is required")
  private String confirmPassword;
}
