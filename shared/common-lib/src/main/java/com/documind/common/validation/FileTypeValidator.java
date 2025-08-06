package com.documind.common.validation;

import com.documind.common.util.FileUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import org.springframework.web.multipart.MultipartFile;

/**
 * Validator implementation for {@link ValidFileType} annotation.
 *
 * <p>Checks that uploaded files:
 *
 * <ul>
 *   <li>Are of an allowed type (extension)
 *   <li>Are within size limits
 * </ul>
 *
 * Null or empty files are considered valid (let {@code @NotNull} handle those cases).
 *
 * @see ValidFileType
 */
public class FileTypeValidator implements ConstraintValidator<ValidFileType, MultipartFile> {

  private String[] allowedTypes;
  private long maxSizeInBytes;

  /**
   * Initializes the validator with constraint values.
   *
   * @param constraintAnnotation the annotation instance
   */
  @Override
  public void initialize(ValidFileType constraintAnnotation) {
    this.allowedTypes = constraintAnnotation.allowedTypes();
    this.maxSizeInBytes = constraintAnnotation.maxSizeInMB() * 1024 * 1024;
  }

  /**
   * Validates the file against type and size constraints.
   *
   * @param file the file to validate
   * @param context validation context
   * @return true if valid, false otherwise
   */
  @Override
  public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
    if (file == null || file.isEmpty()) {
      return true; // Let @NotNull handle null validation
    }

    // Validate file size
    if (file.getSize() > maxSizeInBytes) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(
              "File size exceeds maximum allowed size of "
                  + (maxSizeInBytes / (1024 * 1024))
                  + " MB")
          .addConstraintViolation();
      return false;
    }

    // Validate file type
    String fileExtension = FileUtil.getFileExtension(file.getOriginalFilename());
    boolean isValidType = Arrays.asList(allowedTypes).contains(fileExtension.toLowerCase());

    if (!isValidType) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(
              "Invalid file type '"
                  + fileExtension
                  + "'. Allowed types: "
                  + String.join(", ", allowedTypes))
          .addConstraintViolation();
      return false;
    }

    return true;
  }
}
