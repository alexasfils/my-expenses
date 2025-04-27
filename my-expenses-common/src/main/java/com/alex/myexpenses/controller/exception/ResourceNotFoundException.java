package com.alex.myexpenses.controller.exception;

public class ResourceNotFoundException  extends RuntimeException{

	private static final long serialVersionUID = 6589839191691647919L;
	public ResourceNotFoundException(String message) {
		super(message);
	}

}
