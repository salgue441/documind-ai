package com.documind.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a string is a properly formatted UUID.
 *
 * <p>Can optionally allow null values when {@code allowNull} is set to true.
 *
 * @see UUIDValidator
 */
@Documented
@Constraint(validatedBy = UUIDValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUUID {

  /** Default error message. */
  String message() default "Invalid UUID format";

  /** Validation groups. */
  Class<?>[] groups() default {};

  /** Payload type. */
  Class<? extends Payload>[] payload() default {};

  /**
   * Whether null values should be considered valid.
   *
   * @return true if null values are allowed
   */
  boolean allowNull() default false;
}
