package com.documind.common.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Activity log record for user activities.
 *
 * @param activityId unique activity identifier
 * @param userId user identifier
 * @param activityType type of activity
 * @param description activity description
 * @param timestamp when the activity occurred
 * @param metadata additional activity metadata
 * @param source activity source (web, api, mobile)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ActivityLog(
    @JsonProperty("activity_id") String activityId,
    @JsonProperty("user_id") String userId,
    @JsonProperty("activity_type") String activityType,
    String description,
    LocalDateTime timestamp,
    Map<String, Object> metadata,
    String source) {}
