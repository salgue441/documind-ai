/**
 * Package containing custom validation annotations and validators.
 *
 * <p>This package includes:
 *
 * <ul>
 *   <li>Custom validation annotations ({@link com.documind.common.validation.ValidFileType}, {@link
 *       com.documind.common.validation.ValidUUID})
 *   <li>Validator implementations ({@link com.documind.common.validation.FileTypeValidator}, {@link
 *       com.documind.common.validation.UUIDValidator})
 * </ul>
 *
 * <p>All validators integrate with Jakarta Bean Validation and follow these conventions:
 *
 * <ul>
 *   <li>Support constraint violation messages
 *   <li>Handle null values appropriately
 *   <li>Provide clear error messages
 * </ul>
 */
package com.documind.common.validation;
