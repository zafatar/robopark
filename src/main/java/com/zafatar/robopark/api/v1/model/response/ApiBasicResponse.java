package com.zafatar.robopark.api.v1.model.response;

import org.springframework.http.HttpStatus;

public class ApiBasicResponse {
	private HttpStatus status;
	private String message;

	public ApiBasicResponse() {
		super();
	}

	public ApiBasicResponse(HttpStatus status, String message) {
		super();
		this.setStatus(status);
		this.setMessage(message);
	}

	/**
	 * Getters and setters.
	 */
	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
