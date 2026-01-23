package com.eros.userorderapi.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiErrorType {
	NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found"),
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request"),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
	FORBIDEN(HttpStatus.FORBIDDEN, "Forbidden"),
	INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occured"),
	VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation error");

	private final HttpStatus status;
	private final String defaultMessage;


}
