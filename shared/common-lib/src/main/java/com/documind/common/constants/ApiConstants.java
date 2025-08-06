package com.documind.common.constants;

/**
 * Centralized API-related constants used throughout the application.
 *
 * <p>This class contains:
 *
 * <ul>
 *   <li>API versioning and path prefixes
 *   <li>Standard response messages
 *   <li>HTTP header definitions
 *   <li>Pagination defaults
 *   <li>File upload constraints
 *   <li>Cache and queue names
 * </ul>
 *
 * <p>Constants are organized by functional area and should be used instead of hard-coded values to
 * maintain consistency across the application.
 *
 * @see java.lang.constant.Constable
 */
public final class ApiConstants {

  private ApiConstants() {
    // Prevent instantiation - this is a utility class
  }

  // API Versioning
  /** Current API version (v1). */
  public static final String API_VERSION = "v1";

  /** Base API path prefix (/api/v1). */
  public static final String API_PREFIX = "/api/" + API_VERSION;

  // Response Messages
  /** Standard success message: "Operation completed successfully". */
  public static final String SUCCESS_MESSAGE = "Operation completed successfully";

  /** Resource creation message: "Resource created successfully". */
  public static final String CREATED_MESSAGE = "Resource created successfully";

  /** Resource update message: "Resource updated successfully". */
  public static final String UPDATED_MESSAGE = "Resource updated successfully";

  /** Resource deletion message: "Resource deleted successfully". */
  public static final String DELETED_MESSAGE = "Resource deleted successfully";

  /** Not found message: "Resource not found". */
  public static final String NOT_FOUND_MESSAGE = "Resource not found";

  /** Unauthorized access message: "Unauthorized access". */
  public static final String UNAUTHORIZED_MESSAGE = "Unauthorized access";

  /** Forbidden access message: "Access forbidden". */
  public static final String FORBIDDEN_MESSAGE = "Access forbidden";

  /** Invalid request message: "Invalid request data". */
  public static final String BAD_REQUEST_MESSAGE = "Invalid request data";

  // HTTP Headers
  /** Authorization header name: "Authorization". */
  public static final String AUTHORIZATION_HEADER = "Authorization";

  /** Bearer token prefix: "Bearer ". */
  public static final String BEARER_PREFIX = "Bearer ";

  /** Content-Type header name: "Content-Type". */
  public static final String CONTENT_TYPE_HEADER = "Content-Type";

  /** Content-Disposition header name: "Content-Disposition". */
  public static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition";

  // Pagination
  /** Default page size: 20 items. */
  public static final int DEFAULT_PAGE_SIZE = 20;

  /** Maximum allowed page size: 100 items. */
  public static final int MAX_PAGE_SIZE = 100;

  /** Default page number: 0 (first page). */
  public static final int DEFAULT_PAGE_NUMBER = 0;

  // File Upload
  /** Maximum allowed file size: 10MB (10 * 1024 * 1024 bytes). */
  public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

  /** Allowed image file extensions: png, jpg, jpeg, gif, bmp. */
  public static final String[] ALLOWED_IMAGE_TYPES = {"png", "jpg", "jpeg", "gif", "bmp"};

  /** Allowed document file extensions: pdf, doc, docx, txt, rtf. */
  public static final String[] ALLOWED_DOCUMENT_TYPES = {"pdf", "doc", "docx", "txt", "rtf"};

  // Cache Names
  /** User cache name: "users". */
  public static final String USER_CACHE = "users";

  /** Document cache name: "documents". */
  public static final String DOCUMENT_CACHE = "documents";

  /** Settings cache name: "settings". */
  public static final String SETTINGS_CACHE = "settings";

  // Queue Names
  /** Document processing queue name: "document.processing". */
  public static final String DOCUMENT_PROCESSING_QUEUE = "document.processing";

  /** OCR processing queue name: "ocr.processing". */
  public static final String OCR_PROCESSING_QUEUE = "ocr.processing";

  /** NLP processing queue name: "nlp.processing". */
  public static final String NLP_PROCESSING_QUEUE = "nlp.processing";

  /** Notification queue name: "notifications". */
  public static final String NOTIFICATION_QUEUE = "notifications";

  // Exchange Names
  /** Document exchange name: "document.exchange". */
  public static final String DOCUMENT_EXCHANGE = "document.exchange";

  /** Notification exchange name: "notification.exchange". */
  public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
}
