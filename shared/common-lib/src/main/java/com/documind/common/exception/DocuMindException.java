package com.documind.common.exception;

import lombok.Getter;

/**
 * Base exception class for the DocuMind application.
 *
 * <p>All custom exceptions should extend this class to maintain consistent error handling. Includes
 * an error code for machine processing and optional arguments for message formatting.
 *
 * @see RuntimeException
 */
@Getter
public class DocuMindException extends RuntimeException {

  /** Machine-readable error code identifying the exception type. */
  private final String code;

  /** Optional arguments for message formatting. */
  private final Object[] args;

  /**
   * Constructs a new exception with default error code.
   *
   * @param message the detail message
   */
  public DocuMindException(String message) {
    super(message);
    this.code = "GENERAL_ERROR";
    this.args = null;
  }

  /**
   * Constructs a new exception with specified error code.
   *
   * @param code the error code
   * @param message the detail message
   */
  public DocuMindException(String code, String message) {
    super(message);
    this.code = code;
    this.args = null;
  }

  /**
   * Constructs a new exception with error code and message arguments.
   *
   * @param code the error code
   * @param message the detail message (may contain format placeholders)
   * @param args arguments for message formatting
   */
  public DocuMindException(String code, String message, Object... args) {
    super(message);
    this.code = code;
    this.args = args;
  }

  /**
   * Constructs a new exception with error code and cause.
   *
   * @param code the error code
   * @param message the detail message
   * @param cause the root cause
   */
  public DocuMindException(String code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
    this.args = null;
  }
}
