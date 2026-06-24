package com.specification.service.exception;

public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String message) {
		super(message);
	}

    public ResourceNotFoundException(String message,Exception e) {
        super(message,e);
    }
}
