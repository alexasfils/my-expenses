package com.alex.myexpenses.general;

import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing error information in case of validation or processing
 * issues.
 * <p>
 * This class holds a general error message and a collection of specific
 * field-level errors (e.g., input validation errors), mapping field names to
 * their associated error messages.
 * </p>
 */
@Data
@NoArgsConstructor
public class ErrorDTO {

	/**
	 * A general error message describing the issue. This message provides an
	 * overview of the error encountered during processing.
	 */
	private String message;

	/**
	 * A map of field-specific error messages. The key represents the field name and
	 * the value contains the corresponding error message related to that specific
	 * field.
	 */
	private Map<String, String> fieldErrors;
}
