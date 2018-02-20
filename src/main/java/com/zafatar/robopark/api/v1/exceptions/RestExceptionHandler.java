package com.zafatar.robopark.api.v1.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.zafatar.robopark.api.v1.model.response.ApiBasicResponse;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	// 404 - BoardNotFoundException
	@ExceptionHandler({ BoardNotFoundException.class })
	public ResponseEntity<ApiBasicResponse> handleBoardNotFoundException(final BoardNotFoundException ex, final WebRequest request) {
		final ApiBasicResponse apiError = new ApiBasicResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
		return new ResponseEntity<ApiBasicResponse>(apiError, new HttpHeaders(), apiError.getStatus());
	}
	
	// 507 - BoardMaxNumberReachedException
	@ExceptionHandler({ BoardMaxNumberReachedException.class })
	public ResponseEntity<ApiBasicResponse> handleBoardMaxNumberReachedException(final BoardMaxNumberReachedException ex, final WebRequest request) {
		final ApiBasicResponse apiError = new ApiBasicResponse(HttpStatus.INSUFFICIENT_STORAGE, ex.getLocalizedMessage());
		return new ResponseEntity<ApiBasicResponse>(apiError, new HttpHeaders(), apiError.getStatus());
	}
	
	// 404 - RobotNotFoundException - TODO: code repeating alert with BoardNotFoundException
	@ExceptionHandler({ RobotNotFoundException.class })
	public ResponseEntity<ApiBasicResponse> handleRobotNotFoundException(final RobotNotFoundException ex, final WebRequest request) {
		final ApiBasicResponse apiError = new ApiBasicResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
		return new ResponseEntity<ApiBasicResponse>(apiError, new HttpHeaders(), apiError.getStatus());
	}
	
	// 409 - RobotMoveNotPossibleException
	@ExceptionHandler({ RobotMoveNotPossibleException.class })
	public ResponseEntity<ApiBasicResponse> handleRobotMoveNotPossibleException(final RobotMoveNotPossibleException ex, final WebRequest request) {
		final ApiBasicResponse apiError = new ApiBasicResponse(HttpStatus.CONFLICT, ex.getLocalizedMessage());
		return new ResponseEntity<ApiBasicResponse>(apiError, new HttpHeaders(), apiError.getStatus());
	}
	
	// 500
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ApiBasicResponse> handleAll(final Exception ex, final WebRequest request) {
        final ApiBasicResponse apiError = new ApiBasicResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
        return new ResponseEntity<ApiBasicResponse>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}