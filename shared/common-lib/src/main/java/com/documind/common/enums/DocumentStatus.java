package com.documind.common.enums;

/**
 * Enumeration for document processing status in the DocuMind system.
 *
 * <p>Tracks the lifecycle of a document through various processing stages. Provides utility methods
 * to check status categories.
 */
public enum DocumentStatus {
  /** Document has been successfully uploaded to the system. */
  UPLOADED("Document uploaded successfully"),

  /** Document is currently being processed. */
  PROCESSING("Document is being processed"),

  /** Optical Character Recognition (OCR) processing has completed. */
  OCR_COMPLETE("OCR processing completed"),

  /** Natural Language Processing (NLP) has completed. */
  NLP_COMPLETE("NLP processing completed"),

  /** Document classification has completed. */
  CLASSIFICATION_COMPLETE("Classification completed"),

  /** All processing stages have completed successfully. */
  COMPLETED("All processing completed"),

  /** Processing failed due to an error. */
  FAILED("Processing failed"),

  /** Processing was manually cancelled. */
  CANCELLED("Processing cancelled");

  private final String description;

  DocumentStatus(String description) {
    this.description = description;
  }

  /**
   * Gets the human-readable description of this status.
   *
   * @return status description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Checks if processing is fully completed.
   *
   * @return true if status is COMPLETED
   */
  public boolean isCompleted() {
    return this == COMPLETED;
  }

  /**
   * Checks if processing ended in failure or was cancelled.
   *
   * @return true if status is FAILED or CANCELLED
   */
  public boolean isFailed() {
    return this == FAILED || this == CANCELLED;
  }

  /**
   * Checks if document is in any processing state.
   *
   * @return true if status is PROCESSING, OCR_COMPLETE, NLP_COMPLETE, or CLASSIFICATION_COMPLETE
   */
  public boolean isProcessing() {
    return this == PROCESSING
        || this == OCR_COMPLETE
        || this == NLP_COMPLETE
        || this == CLASSIFICATION_COMPLETE;
  }
}
