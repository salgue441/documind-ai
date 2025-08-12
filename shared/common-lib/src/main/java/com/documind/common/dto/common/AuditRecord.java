package com.documind.common.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Audit trail record for tracking changes.
 *
 * @param entityType type of entity that changed
 * @param entityId identifier of the entity
 * @param action action that was performed
 * @param userId user who performed the action
 * @param timestamp when the action occurred
 * @param oldValues previous values
 * @param newValues new values
 * @param ipAddress IP address of the user
 * @param userAgent browser user agent
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuditRecord(
    @JsonProperty("entity_type") String entityType,
    @JsonProperty("entity_id") String entityId,
    String action,
    @JsonProperty("user_id") String userId,
    LocalDateTime timestamp,
    @JsonProperty("old_values") Map<String, Object> oldValues,
    @JsonProperty("new_values") Map<String, Object> newValues,
    @JsonProperty("ip_address") String ipAddress,
    @JsonProperty("user_agent") String userAgent) {}
