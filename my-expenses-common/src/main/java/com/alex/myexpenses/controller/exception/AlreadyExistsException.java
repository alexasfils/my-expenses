package com.alex.myexpenses.controller.exception;

public class AlreadyExistsException extends RuntimeException {
	
	public AlreadyExistsException(String message) {
        super(message);
    }

}
