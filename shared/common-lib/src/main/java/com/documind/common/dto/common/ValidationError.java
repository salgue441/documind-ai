package com.documind.common.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Validation error record for field-specific errors.
 *
 * @param field field name that failed validation
 * @param value rejected value (masked if sensitive)
 * @param message error message
 * @return error code
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ValidationError(String field, Object value, String message, String code) {
  /** Creates a validation error without exposing the value. */
  public static ValidationError of(String field, String message) {
    return new ValidationError(field, null, message, null);
  }

  /** Creates a validation error with error code. */
  public static ValidationError withCode(String field, String message, String code) {
    return new ValidationError(field, null, message, code);
  }
}
