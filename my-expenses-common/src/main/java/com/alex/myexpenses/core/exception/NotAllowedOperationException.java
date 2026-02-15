package com.alex.myexpenses.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotAllowedOperationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NotAllowedOperationException(String message) {
        super(message);
    }

}
