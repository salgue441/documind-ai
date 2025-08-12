package com.documind.common.dto.common;

/**
 * Generic tuple record for two values.
 *
 * @param first first value
 * @param second second value
 */
public record Tuple<T, U>(T first, U second) {}
