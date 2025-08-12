package com.documind.common.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * External service integration record.
 *
 * @param serviceName name of the external service
 * @param endpoint service endpoint URL
 * @param status integration status
 * @param lastSyncAt last synchronization time
 * @param errorCount number of recent errors
 * @param configuration service configuration
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ExternalServiceIntegration(
    @JsonProperty("service_name") String serviceName,
    String endpoint,
    String status,
    @JsonProperty("last_sync_at") LocalDateTime lastSyncAt,
    @JsonProperty("error_count") Integer errorCount,
    Map<String, Object> configuration) {}
