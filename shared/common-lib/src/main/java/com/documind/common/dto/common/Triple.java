package com.documind.common.dto.common;

/**
 * Generic triple record for three values.
 *
 * @param first first value
 * @param second second value
 * @param third third value
 */
public record Triple<T, U, V>(T first, U second, V third) {}
