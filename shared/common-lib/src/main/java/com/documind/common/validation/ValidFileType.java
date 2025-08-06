package com.documind.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a file's type and size meet specified requirements.
 *
 * <p>Can be applied to {@link org.springframework.web.multipart.MultipartFile} fields, methods, or
 * parameters to enforce file type and size constraints.
 *
 * @see FileTypeValidator
 */
@Documented
@Constraint(validatedBy = FileTypeValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFileType {

  /** Default error message template. */
  String message() default "Invalid file type. Allowed types: {allowedTypes}";

  /** Validation groups. */
  Class<?>[] groups() default {};

  /** Payload type. */
  Class<? extends Payload>[] payload() default {};

  /**
   * Allowed file extensions (e.g., "pdf", "jpg").
   *
   * @return array of allowed file extensions
   */
  String[] allowedTypes() default {"pdf", "doc", "docx", "txt", "png", "jpg", "jpeg"};

  /**
   * Maximum allowed file size in megabytes.
   *
   * @return maximum file size in MB
   */
  long maxSizeInMB() default 10;
}
