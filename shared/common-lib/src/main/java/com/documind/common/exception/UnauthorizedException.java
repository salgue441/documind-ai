package com.documind.common.exception;

/**
 * Exception thrown when authentication fails or access is denied.
 *
 * <p>Automatically sets the error code to "UNAUTHORIZED" and provides default messages for common
 * authorization failures.
 */
public class UnauthorizedException extends DocuMindException {

  /**
   * Constructs a new exception with a custom message.
   *
   * @param message the detail message
   */
  public UnauthorizedException(String message) {
    super("UNAUTHORIZED", message);
  }

  /** Constructs a new exception with default access denied message. */
  public UnauthorizedException() {
    super("UNAUTHORIZED", "Access denied. Authentication required.");
  }
}
