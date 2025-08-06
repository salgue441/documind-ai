package com.documind.common.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Base entity class providing common fields for all entities in the application. Includes auditing
 * fields (creation/modification timestamps, user tracking), UUID primary key, versioning for
 * optimistic locking, and soft delete capability.
 *
 * <p>This abstract class is designed to be extended by all entity classes to ensure consistent
 * behavior across the data model.
 *
 * @see jakarta.persistence.MappedSuperclass
 * @see org.springframework.data.jpa.domain.support.AuditingEntityListener
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

  /**
   * Unique identifier for the entity, generated as UUID. Marked as non-updatable and non-nullable
   * in the database.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  /**
   * Timestamp when the entity was created. Automatically set on creation and cannot be updated
   * afterward. Non-nullable in the database.
   */
  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  /** Timestamp when the entity was last modified. Automatically updated on each modification. */
  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  /** Version number for optimistic locking. Automatically incremented by JPA on each update. */
  @Version
  @Column(name = "version")
  private Long version;

  /** Identifier of the user who created the entity. */
  @Column(name = "created_by")
  private String createdBy;

  /** Identifier of the user who last modified the entity. */
  @Column(name = "updated_by")
  private String updatedBy;

  /** Flag indicating whether the entity is soft-deleted. Defaults to false when not specified. */
  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  /**
   * JPA lifecycle callback method executed before entity persistence. Ensures creation and update
   * timestamps are set and isDeleted has a default value.
   */
  @PrePersist
  protected void onCreate() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
    if (updatedAt == null) {
      updatedAt = LocalDateTime.now();
    }
    if (isDeleted == null) {
      isDeleted = false;
    }
  }

  /**
   * JPA lifecycle callback method executed before entity update. Ensures the update timestamp is
   * refreshed.
   */
  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
