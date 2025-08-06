package com.documind.common.exception;

/**
 * Exception thrown when a requested resource cannot be found.
 *
 * <p>Automatically sets the error code to "RESOURCE_NOT_FOUND" and formats a descriptive message
 * including the resource type and identifier.
 */
public class ResourceNotFoundException extends DocuMindException {

  /**
   * Constructs a new exception for a missing resource.
   *
   * @param resource the type of resource that wasn't found
   * @param identifier the identifier used in the search
   */
  public ResourceNotFoundException(String resource, String identifier) {
    super(
        "RESOURCE_NOT_FOUND",
        String.format("%s not found with identifier: %s", resource, identifier));
  }

  /**
   * Constructs a new exception with a custom message.
   *
   * @param message the detail message
   */
  public ResourceNotFoundException(String message) {
    super("RESOURCE_NOT_FOUND", message);
  }
}
