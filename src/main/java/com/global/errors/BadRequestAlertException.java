package com.global.errors;

import org.springframework.http.HttpStatus;

public class BadRequestAlertException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final HttpStatus status;

	public BadRequestAlertException(String message) {
		super(message);
		this.status = HttpStatus.BAD_REQUEST;
	}

	public HttpStatus getStatus() {
		return status;
	}
}