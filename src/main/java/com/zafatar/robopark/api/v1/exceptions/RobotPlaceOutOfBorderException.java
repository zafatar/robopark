package com.zafatar.robopark.api.v1.exceptions;

/**
 * Custom Robot not found exception class. 
 * 
 * @author zafatar
 *
 */
public class RobotPlaceOutOfBorderException extends Exception {
	private static final long serialVersionUID = 42L;

	public int robotId;
	
	public RobotPlaceOutOfBorderException(int robotId) {
		super("robot #'" + robotId + "' can not be placed out of the board.");
		this.robotId = robotId;
	}
}
