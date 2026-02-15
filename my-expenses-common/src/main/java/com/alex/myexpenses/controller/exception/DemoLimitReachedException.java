package com.alex.myexpenses.controller.exception;

public class DemoLimitReachedException extends RuntimeException {
	
	public DemoLimitReachedException(String message) {
        super(message);
    }
}
