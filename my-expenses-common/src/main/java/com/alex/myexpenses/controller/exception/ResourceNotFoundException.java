package com.alex.myexpenses.controller.exception;

public class ResourceNotFoundException  extends RuntimeException {

	private static final long serialVersionUID = 6589839191691647919L;
	
	private String resourceName;
	private String fieldName;
	private Object fieldValue;
	
	public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
		super(String.format("%s non trovato con %s: '%s'", resourceName, fieldName, fieldValue));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public String getResourceName() {
		return resourceName;
	}

	public String getFildName() {
		return fieldName;
	}

	public Object getFildValue() {
		return fieldValue;
	}

}
