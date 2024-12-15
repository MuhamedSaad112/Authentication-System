package com.global.errors;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@ControllerAdvice
public class GlobalExceptionHandler {

	// InvalidPasswordException
	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<Map<String, Object>> handleInvalidPasswordException(InvalidPasswordException ex) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	// LoginAlreadyUsedException
	@ExceptionHandler(LoginAlreadyUsedException.class)
	public ResponseEntity<Map<String, Object>> handleLoginAlreadyUsedException(LoginAlreadyUsedException ex) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
	}

	// EmailAlreadyUsedException
	@ExceptionHandler(EmailAlreadyUsedException.class)
	public ResponseEntity<Map<String, Object>> handleEmailAlreadyUsedException(EmailAlreadyUsedException ex) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
	}

	// BadRequestAlertException
	@ExceptionHandler(BadRequestAlertException.class)
	public ResponseEntity<Map<String, Object>> handleBadRequestAlertException(BadRequestAlertException ex) {
		return buildErrorResponse(ex.getMessage(), ex.getStatus());
	}

	// BadRequestException
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException ex) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	// ResourceNotFoundException (404 Not Found)
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
	}



	private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
		Map<String, Object> errorDetails = new HashMap<>();
		errorDetails.put("timestamp", LocalDateTime.now());
		errorDetails.put("message", message);
		errorDetails.put("status", status.value());
		errorDetails.put("error", status.getReasonPhrase());
		errorDetails.put("path", ServletUriComponentsBuilder.fromCurrentRequest().toUriString());

		return new ResponseEntity<>(errorDetails, status);
	}
}
