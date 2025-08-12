package com.documind.common.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

/**
 * API error response record following RFC 7807 Problem Details.
 *
 * @param type URI reference that identifies the problem type
 * @param title short, human-readable summary
 * @param status HTTP status code
 * @param detail human-readable explanation
 * @param instance URI reference that identifies the specific occurrence
 * @param timestamp when the error occurred
 * @param requestId unique request identifier
 * @param errors list of validation errors
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
    String type,
    String title,
    Integer status,
    String detail,
    String instance,
    LocalDateTime timestamp,
    @JsonProperty("request_id") String requestId,
    List<ValidationError> errors) {
  /** Creates a simple API error */
  public static ApiError of(String title, Integer status, String detail) {
    return new ApiError(null, title, status, detail, null, LocalDateTime.now(), null, null);
  }

  /** Creates an API error with validation errors. */
  public static ApiError withValidationErrors(
      String title, Integer status, List<ValidationError> errors) {
    return new ApiError(
        null, title, status, "Validation failed", null, LocalDateTime.now(), null, errors);
  }
}
