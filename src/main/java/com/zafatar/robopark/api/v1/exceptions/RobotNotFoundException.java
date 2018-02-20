package com.zafatar.robopark.api.v1.exceptions;

/**
 * Custom Robot not found exception class. 
 * 
 * @author zafatar
 *
 */
public class RobotNotFoundException extends Exception {
	private static final long serialVersionUID = 42L;

	public int robotId;
	
	public RobotNotFoundException(int robotId) {
		super("could not find robot with '" + robotId + "'.");
		this.robotId = robotId;
	}
}
