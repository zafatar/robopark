package com.zafatar.robopark.api.v1.exceptions;

/**
 * Custom - robot move not possible exception
 * 
 * @author zafatar
 *
 */
public final class RobotMoveNotPossibleException extends Exception {
	private static final long serialVersionUID = 9L;
	
	public RobotMoveNotPossibleException(String error) {
		super(error);
	}
}
