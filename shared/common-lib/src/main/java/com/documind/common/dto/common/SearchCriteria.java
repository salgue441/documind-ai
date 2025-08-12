package com.documind.common.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SearchCriteria(
    String field, SearchOperator operator, Object value, List<Object> values) {
  /** Search operators enum */
  public enum SearchOperator {
    EQUALS,
    NOT_EQUALS,
    GREATER_THAN,
    LESS_THAN,
    GREATER_THAN_OR_EQUAL,
    LESS_THAN_OR_EQUAL,
    LIKE,
    NOT_LIKE,
    IN,
    NOT_IN,
    IS_NULL,
    IS_NOT_NULL,
    BETWEEN,
    STARTS_WITH,
    ENDS_WITH,
    CONTAINS
  }

  /** Creates an equals criteria. */
  public static SearchCriteria equals(String field, Object value) {
    return new SearchCriteria(field, SearchOperator.EQUALS, value, null);
  }

  /** Creates a like criteria. */
  public static SearchCriteria like(String field, String value) {
    return new SearchCriteria(field, SearchOperator.LIKE, value, null);
  }

  /** Creates an in criteria. */
  public static SearchCriteria in(String field, List<Object> values) {
    return new SearchCriteria(field, SearchOperator.IN, null, values);
  }
}
