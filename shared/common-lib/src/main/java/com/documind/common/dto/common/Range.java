package com.documind.common.dto.common;

public record Range<T extends Comparable<T>>(T min, T max) {
  /** Compact constructor with validation */
  public Range {
    if (min != null && max != null && min.compareTo(max) > 0) {
      throw new IllegalArgumentException("Min value cannot be greater than max value");
    }
  }

  /** Checks if a value is within the range. */
  public boolean contains(T value) {
    if (value == null) return false;

    boolean aboveMin = min == null || value.compareTo(min) >= 0;
    boolean belowMax = max == null || value.compareTo(max) <= 0;

    return aboveMin && belowMax;
  }
}
