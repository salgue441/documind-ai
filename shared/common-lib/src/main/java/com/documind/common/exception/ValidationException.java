package com.documind.common.exception;

/**
 * Exception thrown when business validation rules are violated.
 *
 * <p>Automatically sets the error code to "VALIDATION_ERROR" and formats messages to include the
 * problematic field when available.
 */
public class ValidationException extends DocuMindException {

  /**
   * Constructs a new exception for a specific field validation failure.
   *
   * @param field the field that failed validation
   * @param message the validation error message
   */
  public ValidationException(String field, String message) {
    super(
        "VALIDATION_ERROR", String.format("Validation failed for field '%s': %s", field, message));
  }

  /**
   * Constructs a new exception with a general validation message.
   *
   * @param message the detail message
   */
  public ValidationException(String message) {
    super("VALIDATION_ERROR", message);
  }
}
