package com.documind.common.dto.common;

/**
 * Sort specification record.
 *
 * @param property property name to sort by
 * @param direction sort direction (ASC/DESC)
 */
public record Sort(String property, Direction direction) {
  /** Sort direction enum. */
  public enum Direction {
    ASC,
    DESC
  }

  /** Creates ascending sort. */
  public static Sort asc(String property) {
    return new Sort(property, Direction.ASC);
  }

  /** Creates descending sort. */
  public static Sort desc(String property) {
    return new Sort(property, Direction.DESC);
  }
}
