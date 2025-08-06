package com.documind.common.exception;

import com.documind.common.dto.BaseResponse;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler that provides consistent error responses across all controllers.
 *
 * <p>Translates exceptions into standardized {@link BaseResponse} format with appropriate HTTP
 * status codes. Handles both custom exceptions and framework-specific exceptions.
 *
 * @see RestControllerAdvice
 * @see ExceptionHandler
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles resource not found exceptions.
   *
   * @param ex the caught exception
   * @param request the web request
   * @return error response with HTTP 404 status
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<BaseResponse<Object>> handleResourceNotFoundException(
      ResourceNotFoundException ex, WebRequest request) {
    log.warn("Resource not found: {}", ex.getMessage());
    return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
  }

  /**
   * Handles validation exceptions.
   *
   * @param ex the caught exception
   * @param request the web request
   * @return error response with HTTP 400 status
   */
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<BaseResponse<Object>> handleValidationException(
      ValidationException ex, WebRequest request) {
    log.warn("Validation error: {}", ex.getMessage());
    return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles unauthorized access exceptions.
   *
   * @param ex the caught exception
   * @param request the web request
   * @return error response with HTTP 401 status
   */
  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<BaseResponse<Object>> handleUnauthorizedException(
      UnauthorizedException ex, WebRequest request) {
    log.warn("Unauthorized access: {}", ex.getMessage());
    return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED);
  }

  /**
   * Handles method argument validation failures.
   *
   * @param ex the caught exception
   * @param request the web request
   * @return error response with HTTP 400 status and field errors
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<BaseResponse<Object>> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, WebRequest request) {
    log.warn("Method argument validation failed: {}", ex.getMessage());
    Map<String, String> errors = extractFieldErrors(ex);
    return buildErrorResponse(
        "VALIDATION_ERROR", "Validation failed", errors.toString(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles Spring binding exceptions that occur during request processing.
   *
   * @param ex the caught BindException
   * @param request the web request
   * @return error response with HTTP 400 status containing field-specific errors
   */
  @ExceptionHandler(BindException.class)
  public ResponseEntity<BaseResponse<Object>> handleBindException(
      BindException ex, WebRequest request) {
    log.warn("Binding error: {}", ex.getMessage());
    Map<String, String> errors = extractFieldErrors(ex);
    return buildErrorResponse(
        "BINDING_ERROR", "Request binding failed", errors.toString(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles constraint violation exceptions from Bean Validation.
   *
   * @param ex the caught ConstraintViolationException
   * @param request the web request
   * @return error response with HTTP 400 status
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<BaseResponse<Object>> handleConstraintViolationException(
      ConstraintViolationException ex, WebRequest request) {
    log.warn("Constraint violation: {}", ex.getMessage());
    return buildErrorResponse(
        "CONSTRAINT_VIOLATION", ex.getMessage(), null, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles uncaught DocuMind-specific exceptions.
   *
   * @param ex the caught DocuMindException
   * @param request the web request
   * @return error response with HTTP 500 status
   */
  @ExceptionHandler(DocuMindException.class)
  public ResponseEntity<BaseResponse<Object>> handleDocuMindException(
      DocuMindException ex, WebRequest request) {
    log.error("DocuMind exception: {}", ex.getMessage(), ex);
    return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Fallback handler for all other uncaught exceptions.
   *
   * @param ex the caught Exception
   * @param request the web request
   * @return error response with HTTP 500 status
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<BaseResponse<Object>> handleGenericException(
      Exception ex, WebRequest request) {
    log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
    return buildErrorResponse(
        "INTERNAL_ERROR", "An unexpected error occurred", null, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Builds a standardized error response for DocuMind exceptions.
   *
   * @param ex the exception to convert to response
   * @param status the HTTP status code to return
   * @return ResponseEntity containing the error response
   */
  private ResponseEntity<BaseResponse<Object>> buildErrorResponse(
      DocuMindException ex, HttpStatus status) {
    BaseResponse.ErrorDetails errorDetails =
        new BaseResponse.ErrorDetails(ex.getCode(), ex.getMessage(), null);
    return ResponseEntity.status(status).body(BaseResponse.error(ex.getMessage(), errorDetails));
  }

  /**
   * Builds a standardized error response with custom details.
   *
   * @param code the error code
   * @param message the error message
   * @param detail additional error details
   * @param status the HTTP status code
   * @return ResponseEntity containing the error response
   */
  private ResponseEntity<BaseResponse<Object>> buildErrorResponse(
      String code, String message, String detail, HttpStatus status) {
    BaseResponse.ErrorDetails errorDetails = new BaseResponse.ErrorDetails(code, message, detail);
    return ResponseEntity.status(status).body(BaseResponse.error(message, errorDetails));
  }

  /**
   * Extracts field errors from binding exceptions.
   *
   * @param ex the BindException containing field errors
   * @return Map of field names to error messages
   */
  private Map<String, String> extractFieldErrors(BindException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              String fieldName = ((FieldError) error).getField();
              errors.put(fieldName, error.getDefaultMessage());
            });
    return errors;
  }
}
