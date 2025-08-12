package com.documind.common.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * Data range record for time-based filtering.
 *
 * @param startDate start of the range
 * @param endDate end of the range
 */
public record DateRange(
    @JsonProperty("start_date") LocalDateTime startDate,
    @JsonProperty("end_date") LocalDateTime endDate) {
  /** Compact constructor with validation */
  public DateRange {
    if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date cannot be after end date");
    }
  }

  /** Checks if a date is within the range. */
  public boolean contains(LocalDateTime date) {
    if (date == null) return false;

    boolean afterStart = startDate == null || !date.isBefore(startDate);
    boolean beforeEnd = endDate == null || !date.isAfter(endDate);

    return afterStart && beforeEnd;
  }

  /** Creates a range for the last N days. */
  public static DateRange lastDays(int days) {
    LocalDateTime now = LocalDateTime.now();
    return new DateRange(now.minusDays(days), now);
  }

  /** Creates a range for the current month. */
  public static DateRange currentMonth() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
    return new DateRange(startOfMonth, now);
  }
}
