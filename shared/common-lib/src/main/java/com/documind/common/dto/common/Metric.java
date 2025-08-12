package com.documind.common.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Performance metric record.
 *
 * @param name metric name
 * @param value metric value
 * @param unit metric unit
 * @param timestamp when the metric was recorded
 * @param tags metric tags for grouping
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Metric(
    String name, Double value, String unit, LocalDateTime timestamp, Map<String, String> tags) {}
