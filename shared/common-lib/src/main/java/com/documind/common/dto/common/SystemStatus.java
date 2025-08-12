package com.documind.common.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * System status record for health monitoring.
 *
 * @param component component name
 * @param status component status (UP, DOWN, DEGRADED)
 * @param responseTime response time in milliseconds
 * @param errorRate error rate percentage
 * @param lastChecked when the status was last checked
 * @param details additional status details
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SystemStatus(
    String component,
    String status,
    @JsonProperty("response_time") Long responseTime,
    @JsonProperty("error_rate") Double errorRate,
    @JsonProperty("last_checked") LocalDateTime lastChecked,
    Map<String, Object> details) {}
