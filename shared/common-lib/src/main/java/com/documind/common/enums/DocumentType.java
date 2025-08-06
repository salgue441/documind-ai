package com.documind.common.enums;

/**
 * Enumeration for document types that can be classified by the system.
 *
 * <p>Each type includes a display name for UI presentation and a detailed description. Provides
 * lookup functionality from string values.
 */
public enum DocumentType {
  /** Financial document for billing purposes. */
  INVOICE("Invoice", "Financial document for billing"),

  /** Legal agreement between parties. */
  CONTRACT("Contract", "Legal agreement document"),

  /** Personal qualifications and work history. */
  RESUME("Resume", "Personal qualification document"),

  /** Business or technical analysis document. */
  REPORT("Report", "Business or technical report"),

  /** Formal or informal correspondence. */
  LETTER("Letter", "Formal or informal correspondence"),

  /** Structured data collection form. */
  FORM("Form", "Structured data collection document"),

  /** Official certification document. */
  CERTIFICATE("Certificate", "Official certification document"),

  /** Slide-based presentation materials. */
  PRESENTATION("Presentation", "Slide-based presentation document"),

  /** Instructional or reference material. */
  MANUAL("Manual", "Instructional or reference document"),

  /** Catch-all for unclassified documents. */
  OTHER("Other", "Document type not classified");

  private final String displayName;
  private final String description;

  DocumentType(String displayName, String description) {
    this.displayName = displayName;
    this.description = description;
  }

  /**
   * Gets the display-friendly name of this document type.
   *
   * @return display name suitable for UI presentation
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Gets the detailed description of this document type.
   *
   * @return type description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Converts a string value to a DocumentType.
   *
   * @param value the string to convert (can be enum name or display name)
   * @return matching DocumentType, or OTHER if no match found
   */
  public static DocumentType fromString(String value) {
    for (DocumentType type : DocumentType.values()) {
      if (type.name().equalsIgnoreCase(value) || type.displayName.equalsIgnoreCase(value)) {
        return type;
      }
    }
    return OTHER;
  }
}
