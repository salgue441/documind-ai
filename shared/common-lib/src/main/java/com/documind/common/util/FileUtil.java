package com.documind.common.util;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Utility class for file operations.
 *
 * <p>Provides methods for file validation, name generation, and file information extraction.
 * Includes constants for allowed file types and maximum file size. All methods are static and the
 * class cannot be instantiated.
 */
public class FileUtil {

  /** List of allowed document file extensions (pdf, doc, docx, txt, rtf). */
  public static final List<String> ALLOWED_DOCUMENT_TYPES =
      Arrays.asList("pdf", "doc", "docx", "txt", "rtf");

  /** List of allowed image file extensions (png, jpg, jpeg, gif, bmp, tiff). */
  public static final List<String> ALLOWED_IMAGE_TYPES =
      Arrays.asList("png", "jpg", "jpeg", "gif", "bmp", "tiff");

  /** Maximum allowed file size (10MB). */
  public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

  private FileUtil() {
    // Prevent instantiation
  }

  /**
   * Extracts the file extension from a file name.
   *
   * @param fileName the file name to process
   * @return file extension in lowercase, or empty string if none found
   */
  public static String getFileExtension(String fileName) {
    if (StringUtils.isBlank(fileName)) {
      return "";
    }
    int lastDotIndex = fileName.lastIndexOf('.');
    return lastDotIndex > 0 ? fileName.substring(lastDotIndex + 1).toLowerCase() : "";
  }

  /**
   * Extracts the file extension from a {@link MultipartFile}.
   *
   * @param file the multipart file
   * @return file extension in lowercase, or empty string if none found
   */
  public static String getFileExtension(MultipartFile file) {
    return getFileExtension(file.getOriginalFilename());
  }

  /**
   * Checks if a file has an allowed document extension.
   *
   * @param fileName the file name to check
   * @return true if the extension is in {@link #ALLOWED_DOCUMENT_TYPES}, false otherwise
   */
  public static boolean isValidDocumentType(String fileName) {
    String extension = getFileExtension(fileName);
    return ALLOWED_DOCUMENT_TYPES.contains(extension);
  }

  /**
   * Checks if a file has an allowed image extension.
   *
   * @param fileName the file name to check
   * @return true if the extension is in {@link #ALLOWED_IMAGE_TYPES}, false otherwise
   */
  public static boolean isValidImageType(String fileName) {
    String extension = getFileExtension(fileName);
    return ALLOWED_IMAGE_TYPES.contains(extension);
  }

  /**
   * Checks if a file size is within the allowed limit.
   *
   * @param file the file to check
   * @return true if file size ≤ {@link #MAX_FILE_SIZE}, false otherwise
   */
  public static boolean isValidFileSize(MultipartFile file) {
    return file.getSize() <= MAX_FILE_SIZE;
  }

  /**
   * Generates a unique file name preserving the original extension.
   *
   * @param originalFileName the original file name
   * @return unique file name with format "UUID.extension"
   */
  public static String generateUniqueFileName(String originalFileName) {
    String extension = getFileExtension(originalFileName);
    String uniqueId = UUID.randomUUID().toString();
    return StringUtils.isNotBlank(extension) ? uniqueId + "." + extension : uniqueId;
  }

  /**
   * Sanitizes a file name by replacing invalid characters with underscores.
   *
   * @param fileName the file name to sanitize
   * @return sanitized file name with only alphanumeric, dots, underscores and hyphens
   */
  public static String sanitizeFileName(String fileName) {
    if (StringUtils.isBlank(fileName)) {
      return "unnamed";
    }
    return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
  }

  /**
   * Formats a file size in bytes to human-readable format (KB, MB, GB).
   *
   * @param sizeInBytes the file size in bytes
   * @return formatted string with appropriate unit
   */
  public static String formatFileSize(long sizeInBytes) {
    if (sizeInBytes < 1024) {
      return sizeInBytes + " bytes";
    } else if (sizeInBytes < 1024 * 1024) {
      return String.format("%.1f KB", sizeInBytes / 1024.0);
    } else if (sizeInBytes < 1024 * 1024 * 1024) {
      return String.format("%.1f MB", sizeInBytes / (1024.0 * 1024));
    } else {
      return String.format("%.1f GB", sizeInBytes / (1024.0 * 1024 * 1024));
    }
  }

  /**
   * Validates a file for null/empty check, size limit, and allowed types.
   *
   * @param file the file to validate
   * @throws IllegalArgumentException if the file is invalid with appropriate message
   */
  public static void validateFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("File cannot be null or empty");
    }

    if (!isValidFileSize(file)) {
      throw new IllegalArgumentException(
          "File size exceeds maximum allowed size of " + formatFileSize(MAX_FILE_SIZE));
    }

    String fileName = file.getOriginalFilename();
    if (!isValidDocumentType(fileName) && !isValidImageType(fileName)) {
      throw new IllegalArgumentException(
          "Unsupported file type. Allowed types: "
              + String.join(", ", ALLOWED_DOCUMENT_TYPES)
              + ", "
              + String.join(", ", ALLOWED_IMAGE_TYPES));
    }
  }
}
