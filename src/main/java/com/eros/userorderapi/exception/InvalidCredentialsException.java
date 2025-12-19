package com.eros.userorderapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidCredentialsException extends RuntimeException {

	private static final long serialVersionUID = -1480157718046834386L;

	public InvalidCredentialsException(String message) {
		super(message);
	}
}
