package com.documind.common.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * Application settings record.
 *
 * @param key setting key
 * @param value setting value
 * @param description setting description
 * @param type value type (string, number, boolean, json)
 * @param isSecret whether the setting contains sensitive data
 * @param updatedAt when the setting was last updated
 * @param updatedBy who updated the setting
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AppSetting(
    String key,
    Object value,
    String description,
    String type,
    @JsonProperty("is_secret") Boolean isSecret,
    @JsonProperty("updated_at") LocalDateTime updatedAt,
    @JsonProperty("updated_by") String updatedBy) {}
