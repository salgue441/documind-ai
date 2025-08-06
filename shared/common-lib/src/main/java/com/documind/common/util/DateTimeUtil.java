package com.documind.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for date and time operations.
 *
 * <p>Provides methods for formatting, parsing, comparing, and manipulating {@link LocalDateTime}
 * instances. All methods are static and the class cannot be instantiated.
 *
 * @see LocalDateTime
 * @see DateTimeFormatter
 * @see ChronoUnit
 */
public class DateTimeUtil {

  /** Default date-time format pattern (yyyy-MM-dd HH:mm:ss). */
  public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  /** Default date-time formatter using {@link #DEFAULT_DATE_TIME_FORMAT} pattern. */
  public static final DateTimeFormatter DEFAULT_FORMATTER =
      DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);

  private DateTimeUtil() {
    // Prevent instantiation
  }

  /**
   * Formats a {@link LocalDateTime} using the default formatter.
   *
   * @param dateTime the date-time to format, may be null
   * @return formatted string or null if input is null
   */
  public static String format(LocalDateTime dateTime) {
    return dateTime != null ? dateTime.format(DEFAULT_FORMATTER) : null;
  }

  /**
   * Formats a {@link LocalDateTime} using the specified pattern.
   *
   * @param dateTime the date-time to format, may be null
   * @param pattern the pattern to use for formatting
   * @return formatted string or null if input is null
   * @throws IllegalArgumentException if the pattern is invalid
   */
  public static String format(LocalDateTime dateTime, String pattern) {
    if (dateTime == null) return null;
    return dateTime.format(DateTimeFormatter.ofPattern(pattern));
  }

  /**
   * Parses a date-time string using the default formatter.
   *
   * @param dateTimeString the string to parse
   * @return parsed LocalDateTime
   * @throws java.time.format.DateTimeParseException if the text cannot be parsed
   */
  public static LocalDateTime parse(String dateTimeString) {
    return LocalDateTime.parse(dateTimeString, DEFAULT_FORMATTER);
  }

  /**
   * Parses a date-time string using the specified pattern.
   *
   * @param dateTimeString the string to parse
   * @param pattern the pattern to use for parsing
   * @return parsed LocalDateTime
   * @throws java.time.format.DateTimeParseException if the text cannot be parsed
   * @throws IllegalArgumentException if the pattern is invalid
   */
  public static LocalDateTime parse(String dateTimeString, String pattern) {
    return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(pattern));
  }

  /**
   * Calculates the number of days between two date-times.
   *
   * @param start the start date-time
   * @param end the end date-time
   * @return number of days between start and end
   */
  public static long getDaysBetween(LocalDateTime start, LocalDateTime end) {
    return ChronoUnit.DAYS.between(start, end);
  }

  /**
   * Calculates the number of hours between two date-times.
   *
   * @param start the start date-time
   * @param end the end date-time
   * @return number of hours between start and end
   */
  public static long getHoursBetween(LocalDateTime start, LocalDateTime end) {
    return ChronoUnit.HOURS.between(start, end);
  }

  /**
   * Calculates the number of minutes between two date-times.
   *
   * @param start the start date-time
   * @param end the end date-time
   * @return number of minutes between start and end
   */
  public static long getMinutesBetween(LocalDateTime start, LocalDateTime end) {
    return ChronoUnit.MINUTES.between(start, end);
  }

  /**
   * Checks if a date-time is in the past (expired).
   *
   * @param dateTime the date-time to check
   * @return true if the date-time is before now, false otherwise or if null
   */
  public static boolean isExpired(LocalDateTime dateTime) {
    return dateTime != null && dateTime.isBefore(LocalDateTime.now());
  }

  /**
   * Adds hours to a date-time.
   *
   * @param dateTime the base date-time
   * @param hours the number of hours to add
   * @return new date-time with hours added
   */
  public static LocalDateTime addHours(LocalDateTime dateTime, long hours) {
    return dateTime.plusHours(hours);
  }

  /**
   * Adds days to a date-time.
   *
   * @param dateTime the base date-time
   * @param days the number of days to add
   * @return new date-time with days added
   */
  public static LocalDateTime addDays(LocalDateTime dateTime, long days) {
    return dateTime.plusDays(days);
  }
}
