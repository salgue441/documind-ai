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
import java.util.regex.Pattern;

/**
 * Username validation with business rules.
 *
 * <p>Validates:
 *
 * <ul>
 *   <li>Length between 3-50 characters
 *   <li>Only alphanumeric characters and underscores
 *   <li>Cannot start or end with underscore
 *   <li>Cannot contain consecutive underscores
 *   <li>Reserved username checking
 * </ul>
 */
@Documented
@Constraint(validatedBy = ValidUsernameValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
@NotBlank(message = "Username is required")
public @interface ValidUsername {
  String message() default "Invalid username format";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /**
   * Check for reserved usernames.
   *
   * @return true if reserved username check is enabled
   */
  boolean checkReserved() default true;
}

/** Username validation implementation. */
class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {
  private static final Pattern USERNAME_PATTERN =
      Pattern.compile("^[a-zA-Z0-9]([a-zA-Z0-9_]*[a-zA-Z0-9])?$");

  private static final String[] RESERVED_USERNAMES = {
    "admin",
    "administrator",
    "root",
    "system",
    "api",
    "www",
    "mail",
    "email",
    "support",
    "help",
    "info",
    "sales",
    "marketing",
    "noreply",
    "no-reply",
    "service",
    "services",
    "user",
    "users",
    "guest",
    "public",
    "private",
    "secure",
    "security",
    "test",
    "testing",
    "demo",
    "sample",
    "null",
    "undefined",
    "anonymous",
    "anon",
    "bot",
    "robot",
    "crawler"
  };

  private boolean checkReserved;

  @Override
  public void initialize(ValidUsername constraintAnnotation) {
    this.checkReserved = constraintAnnotation.checkReserved();
  }

  @Override
  public boolean isValid(String username, ConstraintValidatorContext context) {
    if (username == null || username.isBlank()) {
      return false;
    }

    if (!USERNAME_PATTERN.matcher(username).matches()) {
      addViolation(
          context,
          "Username can only contain letters, numbers, and underscores. "
              + "Cannot start or end with underscore or contain consecutive underscores.");

      return false;
    }

    if (username.contains("__")) {
      addViolation(context, "Username cannot contain consecutive underscores.");
      return false;
    }

    if (checkReserved && isReservedUsername(username)) {
      addViolation(context, "This username is reserved. Please choose another.");
      return false;
    }

    return true;
  }

  private void addViolation(ConstraintValidatorContext context, String message) {
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
  }

  private boolean isReservedUsername(String username) {
    String lowerUsername = username.toLowerCase();
    for (String reserved : RESERVED_USERNAMES) {
      if (lowerUsername.equals(reserved)) {
        return true;
      }
    }

    return false;
  }
}
