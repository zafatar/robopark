package com.zafatar.robopark.api.v1.exceptions;

/**
 * Custom Board not found exception class. 
 * 
 * @author zafatar
 *
 */
public final class BoardNotFoundException extends Exception {
	private static final long serialVersionUID = 8L;

	public int boardId;
	
	public BoardNotFoundException(int id) {
		super("could not find board with '" + id + "'.");
		this.boardId = id;
	}
}
