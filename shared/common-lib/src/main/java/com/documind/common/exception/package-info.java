/**
 * Package containing custom exceptions and global exception handling for the DocuMind application.
 *
 * <p>This package includes:
 *
 * <ul>
 *   <li>The base {@link com.documind.common.exception.DocuMindException} class
 *   <li>Specialized business exceptions (validation, not found, etc.)
 *   <li>The {@link com.documind.common.exception.GlobalExceptionHandler} for consistent API error
 *       responses
 * </ul>
 *
 * <p>All custom exceptions extend {@code DocuMindException} and follow these conventions:
 *
 * <ul>
 *   <li>Include an error code for machine-readable error identification
 *   <li>Provide human-readable messages
 *   <li>Support optional arguments for message formatting
 * </ul>
 */
package com.documind.common.exception;
