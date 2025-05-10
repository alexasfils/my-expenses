package com.alex.myexpenses.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BadRequestException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	private final String value;

	public BadRequestException(String message, Throwable cause, String value) {
		super(message, cause);
		this.value = value;
	}
}
