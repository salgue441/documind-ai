package com.documind.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base response wrapper for all API responses in the application. Provides consistent response
 * structure across all services, including success status, message, data payload, error details,
 * and metadata.
 *
 * <p>This class supports both successful and error responses with appropriate factory methods for
 * each case. The generic type parameter allows for type-safe data payloads.
 *
 * @param <T> the type of the data payload included in the response
 * @see JsonInclude
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {

  /** Flag indicating whether the request was successful. */
  private boolean success;

  /** Human-readable message describing the response outcome. */
  private String message;

  /** The actual data payload of the response. May be null for error responses. */
  private T data;

  /** Detailed error information, present only when success is false. */
  private ErrorDetails error;

  /** Timestamp when the response was generated. */
  private LocalDateTime timestamp;

  /** Unique identifier for the request, useful for tracing. */
  private String requestId;

  /**
   * Creates a successful response with data payload.
   *
   * @param <T> the type of the data payload
   * @param data the successful response data
   * @return a successful BaseResponse instance
   */
  public static <T> BaseResponse<T> success(T data) {
    return new BaseResponse<>(true, "Success", data, null, LocalDateTime.now(), null);
  }

  /**
   * Creates a successful response with data payload and custom message.
   *
   * @param <T> the type of the data payload
   * @param data the successful response data
   * @param message custom success message
   * @return a successful BaseResponse instance
   */
  public static <T> BaseResponse<T> success(T data, String message) {
    return new BaseResponse<>(true, message, data, null, LocalDateTime.now(), null);
  }

  /**
   * Creates an error response with a message.
   *
   * @param <T> the type parameter
   * @param message error message describing the failure
   * @return an error BaseResponse instance
   */
  public static <T> BaseResponse<T> error(String message) {
    return new BaseResponse<>(false, message, null, null, LocalDateTime.now(), null);
  }

  /**
   * Creates an error response with a message and detailed error information.
   *
   * @param <T> the type parameter
   * @param message error message describing the failure
   * @param error detailed error information
   * @return an error BaseResponse instance with details
   */
  public static <T> BaseResponse<T> error(String message, ErrorDetails error) {
    return new BaseResponse<>(false, message, null, error, LocalDateTime.now(), null);
  }

  /**
   * Inner class representing detailed error information for failed responses. Includes error code,
   * detailed description, and optional field reference.
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ErrorDetails {
    /** Machine-readable error code. */
    private String code;

    /** Human-readable detailed error description. */
    private String detail;

    /** Optional field name associated with the error. */
    private String field;
  }
}
