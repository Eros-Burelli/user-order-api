package com.eros.userorderapi.exception;

public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 5043038501891677338L;

	public ResourceNotFoundException(String message) {
		super(message);
	}
}
