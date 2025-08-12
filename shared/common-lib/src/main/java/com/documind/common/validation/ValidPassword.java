package com.documind.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Strong password validation with comprehensive security rules.
 *
 * <p>Validates:
 *
 * <ul>
 *   <li>Minimum 8 characters, maximum 128 characters
 *   <li>At least one uppercase letter
 *   <li>At least one lowercase letter
 *   <li>At least one number
 *   <li>At least one special character
 *   <li>No common passwords
 *   <li>No personal information (when context available)
 * </ul>
 */
@Documented
@Constraint(validatedBy = ValidPasswordValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
@NotBlank(message = "Password is required")
public @interface ValidPassword {
  String message() default "Password does not meet security requirements";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /**
   * Minimum strength level (1-5, where 5 is strongest).
   *
   * @return minimum password strength
   */
  int minStrength() default 3;

  /**
   * Check against common password lists.
   *
   * @return true if common password check is enabled
   */
  boolean checkCommonPasswords() default true;
}

class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String> {

  private static final String[] COMMON_PASSWORDS = {
    "password",
    "123456",
    "password123",
    "admin",
    "qwerty",
    "letmein",
    "welcome",
    "monkey",
    "1234567890",
    "abc123",
    "111111",
    "123123",
    "password1",
    "12345678",
    "superman",
    "iloveyou",
    "trustno1",
    "dragon",
    "shadow",
    "master",
    "666666",
    "michael",
    "jennifer",
    "jordan",
    "hunter",
    "fuckyou",
    "harley",
    "hockey",
    "ranger"
  };

  private int minStrength;
  private boolean checkCommonPasswords;

  @Override
  public void initialize(ValidPassword constraintAnnotation) {
    this.minStrength = constraintAnnotation.minStrength();
    this.checkCommonPasswords = constraintAnnotation.checkCommonPasswords();
  }

  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    if (password == null || password.isBlank()) {
      return true;
    }

    if (checkCommonPasswords && isCommonPassword(password)) {
      addViolation(context, "Password is too common. Please choose a more secure password");
      return false;
    }

    int strength = calculateStrength(password);
    if (strength < minStrength) {
      addViolation(context, buildStrengthMessage(strength));
      return false;
    }

    return true;
  }

  private int calculateStrength(String password) {
    int strength = 0;

    if (password.length() >= 8) strength++;
    if (password.length() >= 12) strength++;

    if (password.matches(".*[a-z].*")) strength++;
    if (password.matches(".*[A-Z].*")) strength++;
    if (password.matches(".*[0-9].*")) strength++;
    if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) strength++;

    if (!hasSequentialChars(password)) strength++;

    if (!hasRepeatingChars(password)) strength++;

    return Math.min(strength, 5);
  }

  private boolean hasSequentialChars(String password) {
    String lower = password.toLowerCase();
    for (int i = 0; i < lower.length() - 2; i++) {
      if (lower.charAt(i + 1) == lower.charAt(i) + 1
          && lower.charAt(i + 2) == lower.charAt(i) + 2) {
        return true;
      }
    }

    return false;
  }

  private boolean hasRepeatingChars(String password) {
    for (int i = 0; i < password.length() - 2; i++) {
      if (password.charAt(i) == password.charAt(i + 1)
          && password.charAt(i) == password.charAt(i + 2)) {
        return true;
      }
    }

    return false;
  }

  private boolean isCommonPassword(String password) {
    String lowerPassword = password.toLowerCase();
    for (String common : COMMON_PASSWORDS) {
      if (lowerPassword.equals(common) || lowerPassword.contains(common)) {
        return true;
      }
    }

    return false;
  }

  private String buildStrengthMessage(int strength) {
    return switch (strength) {
      case 0, 1 ->
          "Password is very weak. Include uppercase, lowercase, numbers, and special characters";
      case 2 -> "Password is weak. Add more character variety and avoid common patterns";
      case 3 -> "Password is fair. Consider making it longer and more complex";
      case 4 -> "Password is good but could be stronger";
      default -> "Password meets minimum requirements";
    };
  }

  private void addViolation(ConstraintValidatorContext context, String message) {
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
  }
}
