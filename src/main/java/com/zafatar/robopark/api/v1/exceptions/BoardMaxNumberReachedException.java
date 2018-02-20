package com.zafatar.robopark.api.v1.exceptions;

/**
 * Custom - max number of board reached exception class. 
 * 
 * @author zafatar
 *
 */
public final class BoardMaxNumberReachedException extends Exception {
	private static final long serialVersionUID = 8L;
	
	public BoardMaxNumberReachedException() {
		super("max number of boards reached. Can not create a new one.");
	}
}
