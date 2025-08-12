package com.documind.common.dto.common;

/**
 * Generic key-value pair record.
 *
 * @param key the key
 * @param value the value
 */
public record KeyValue<K, V>(K key, V value) {}
