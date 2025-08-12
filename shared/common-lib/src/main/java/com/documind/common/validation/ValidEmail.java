package com.documind.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

/**
 * Enhanced email validation with business rules.
 *
 * <p>Validates:
 *
 * <ul>
 *   <li>Standard email format (RFC 5322)
 *   <li>Domain whitelist/blacklist support
 *   <li>Disposable email detection
 *   <li>Length constraints
 * </ul>
 */
@Documented
@Constraint(validatedBy = ValidEmailValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Email(message = "Invalid email format")
@NotBlank(message = "Email cannot be blank")
public @interface ValidEmail {
  String message() default "Invalid email format";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /**
   * Allow disposable email addresses.
   *
   * @return true if disposable emails are allowed.
   */
  boolean allowDisposable() default false;

  /**
   * Allowed email domains (empty means all allowed).
   *
   * @return array of allowed domains.
   */
  String[] allowedDomains() default {};

  /**
   * Blocked email domains.
   *
   * @return array of blocked domains.
   */
  String[] blockedDomains() default {
    "tempmail.org",
    "10minutemail.com",
    "guerrillamail.com",
    "mailinator.com",
    "throwawaymail.com",
    "yopmail.com",
    "getnada.com",
    "dispostable.com",
    "maildrop.cc",
    "fakeinbox.com",
    "trashmail.com",
    "mintemail.com",
    "mytemp.email",
    "spambox.us",
    "mailcatch.com",
    "emailondeck.com",
    "mailnesia.com",
    "disposablemail.com",
    "easytrashmail.com",
    "anonymbox.com",
    "temp-mail.io",
  };
}

/** Email validator implementation. */
class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {
  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

  private boolean allowDisposable;
  private String[] allowedDomains;
  private String[] blockedDomains;

  @Override
  public void initialize(ValidEmail constraintAnnotation) {
    this.allowDisposable = constraintAnnotation.allowDisposable();
    this.allowedDomains = constraintAnnotation.allowedDomains();
    this.blockedDomains = constraintAnnotation.blockedDomains();
  }

  @Override
  public boolean isValid(String email, ConstraintValidatorContext context) {
    if (email == null || email.isBlank()) {
      return false;
    }

    if (!EMAIL_PATTERN.matcher(email).matches()) {
      addViolation(context, "Invalid email format");
      return false;
    }

    if (email.length() > 254) {
      addViolation(context, "Email exceeds maximum length of 254 characters");
      return false;
    }

    String domain = email.substring(email.indexOf('@') + 1).toLowerCase();
    if (allowedDomains.length > 0) {
      boolean domainAllowed = false;
      for (String allowedDomain : allowedDomains) {
        if (domain.equals(allowedDomain.toLowerCase())) {
          domainAllowed = true;
          break;
        }
      }

      if (!domainAllowed) {
        addViolation(context, "Email domain is not allowed");
        return false;
      }
    }

    for (String blockedDomain : blockedDomains) {
      if (domain.equals(blockedDomain.toLowerCase())) {
        addViolation(context, "Disposable email addresses are not allowed");
        return false;
      }
    }

    if (!allowDisposable && isDisposableEmail(domain)) {
      addViolation(context, "Disposable email addresses are not allowed");
      return false;
    }

    return true;
  }

  private void addViolation(ConstraintValidatorContext context, String message) {
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
  }

  private boolean isDisposableEmail(String domain) {
    String[] disposablePatterns = {
      "tempmail.org",
      "10minutemail.com",
      "guerrillamail.com",
      "mailinator.com",
      "throwawaymail.com",
      "yopmail.com",
      "getnada.com",
      "dispostable.com",
      "maildrop.cc",
      "fakeinbox.com",
      "trashmail.com",
      "mintemail.com",
      "mytemp.email",
      "spambox.us",
      "mailcatch.com",
      "emailondeck.com",
      "mailnesia.com",
      "disposablemail.com",
      "easytrashmail.com",
      "anonymbox.com",
      "temp-mail.io"
    };

    for (String pattern : disposablePatterns) {
      if (domain.contains(pattern)) {
        return true;
      }
    }

    return false;
  }
}
