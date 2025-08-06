package com.documind.common.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

/**
 * Paginated response wrapper for API endpoints returning lists of items. Encapsulates both the
 * content and pagination metadata for consistent paginated responses across the application.
 *
 * <p>This class provides a factory method to easily convert from Spring Data's {@link Page}
 * interface to this standardized response format.
 *
 * @param <T> the type of elements in the paginated content
 * @see org.springframework.data.domain.Page
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

  /** The actual content of the current page. */
  private List<T> content;

  /** Zero-based page index. */
  private int page;

  /** Number of items per page. */
  private int size;

  /** Total number of elements across all pages. */
  private long totalElements;

  /** Total number of pages available. */
  private int totalPages;

  /** Flag indicating whether the current page is the first one. */
  private boolean first;

  /** Flag indicating whether the current page is the last one. */
  private boolean last;

  /** Flag indicating whether a next page exists. */
  private boolean hasNext;

  /** Flag indicating whether a previous page exists. */
  private boolean hasPrevious;

  /**
   * Factory method to create a PageResponse from a Spring Data Page.
   *
   * @param <T> the type of elements in the page
   * @param page the source Page object from Spring Data
   * @return a populated PageResponse instance
   */
  public static <T> PageResponse<T> of(Page<T> page) {
    return new PageResponse<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages(),
        page.isFirst(),
        page.isLast(),
        page.hasNext(),
        page.hasPrevious());
  }
}
