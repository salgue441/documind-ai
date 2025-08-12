package com.documind.common.dto.common;

import java.util.List;

public record FilterSpec(List<SearchCriteria> criteria, LogicalOperator operator) {
  /** Logical operators for combining criteria. */
  public enum LogicalOperator {
    AND,
    OR
  }

  /** Creates an AND filter. */
  public static FilterSpec and(List<SearchCriteria> criteria) {
    return new FilterSpec(criteria, LogicalOperator.AND);
  }

  /** Creates an OR filter. */
  public static FilterSpec or(List<SearchCriteria> criteria) {
    return new FilterSpec(criteria, LogicalOperator.OR);
  }
}
