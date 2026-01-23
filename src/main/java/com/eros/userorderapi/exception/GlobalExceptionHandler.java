package com.eros.userorderapi.exception;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.eros.userorderapi.dto.response.ApiErrorResponse;
import com.eros.userorderapi.enums.ApiErrorType;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
		return buildErrorResponse(ApiErrorType.NOT_FOUND, ex.getMessage(), request);
	}

	@ExceptionHandler({IllegalArgumentException.class, InvalidCredentialsException.class})
	public ResponseEntity<ApiErrorResponse> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
		return buildErrorResponse(ApiErrorType.BAD_REQUEST, ex.getMessage(), request);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request){
		String message = ex.getBindingResult()
						   .getFieldErrors()
						   .stream()
						   .map(err -> err.getField() + ": " + err.getDefaultMessage())
						   .findFirst()
						   .orElse("Validation error");

		return buildErrorResponse(ApiErrorType.VALIDATION_ERROR, message, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
		return buildErrorResponse(ApiErrorType.INTERNAL_ERROR, null, request);
	}

	private ResponseEntity<ApiErrorResponse> buildErrorResponse(ApiErrorType type, String details, HttpServletRequest request) {
		ApiErrorResponse response = new ApiErrorResponse(
				LocalDateTime.now(),
				type.getStatus().value(),
				type.getDefaultMessage(),
				details != null ? details : type.getDefaultMessage(),
				request.getRequestURI());

		return ResponseEntity.status(type.getStatus()).body(response);
	}

}
