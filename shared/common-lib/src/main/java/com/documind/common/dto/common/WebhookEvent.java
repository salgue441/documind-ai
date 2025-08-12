package com.documind.common.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Webhook event record.
 *
 * @param eventId unique event identifier
 * @param eventType event type
 * @param payload event payload
 * @param signature webhook signature for verification
 * @param timestamp when the event occurred
 * @param retryCount number of delivery attempts
 * @param status delivery status
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record WebhookEvent(
    @JsonProperty("event_id") String eventId,
    @JsonProperty("event_type") String eventType,
    Map<String, Object> payload,
    String signature,
    LocalDateTime timestamp,
    @JsonProperty("retry_count") Integer retryCount,
    String status) {}
