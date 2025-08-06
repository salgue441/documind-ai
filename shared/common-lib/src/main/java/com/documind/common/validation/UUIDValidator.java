package com.documind.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.UUID;

/**
 * Validator implementation for {@link ValidUUID} annotation.
 *
 * <p>Validates that a string:
 *
 * <ul>
 *   <li>Is a properly formatted UUID (when not null)
 *   <li>Optionally allows null values based on annotation configuration
 * </ul>
 *
 * @see ValidUUID
 */
public class UUIDValidator implements ConstraintValidator<ValidUUID, String> {

  private boolean allowNull;

  /**
   * Initializes the validator with constraint values.
   *
   * @param constraintAnnotation the annotation instance
   */
  @Override
  public void initialize(ValidUUID constraintAnnotation) {
    this.allowNull = constraintAnnotation.allowNull();
  }

  /**
   * Validates the UUID string format.
   *
   * @param value the string to validate
   * @param context validation context
   * @return true if valid UUID or allowed null, false otherwise
   */
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return allowNull;
    }

    if (value.trim().isEmpty()) {
      return false;
    }

    try {
      UUID.fromString(value);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}
