package com.zafatar.robopark.api.v1.model.response;

import org.springframework.http.HttpStatus;

public class ApiResponse extends ApiBasicResponse {
	private Object data;

	public ApiResponse() {
		super();
	}

	public ApiResponse(HttpStatus status, String message, Object data) {
		super();
		this.setStatus(status);
		this.setMessage(message);
		this.setData(data);
	}

	/**
	 * Getters and setters.
	 */
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
