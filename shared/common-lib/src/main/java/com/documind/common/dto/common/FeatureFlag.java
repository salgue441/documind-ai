package com.documind.common.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/**
 * Feature flag record for controlling application features.
 *
 * @param name feature name
 * @param enabled whether the feature is enabled
 * @param description feature description
 * @param rolloutPercentage percentage of users who should see this feature
 * @param conditions conditions under which the feature is enabled
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FeatureFlag(
    String name,
    Boolean enabled,
    String description,
    @JsonProperty("rollout_percentage") Integer rolloutPercentage,
    Map<String, Object> conditions) {}
