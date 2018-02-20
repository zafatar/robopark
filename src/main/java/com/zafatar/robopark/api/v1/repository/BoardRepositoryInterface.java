package com.zafatar.robopark.api.v1.repository;

import java.util.Map;

import com.zafatar.robopark.api.v1.exceptions.BoardMaxNumberReachedException;
import com.zafatar.robopark.api.v1.exceptions.BoardNotFoundException;
import com.zafatar.robopark.api.v1.model.Board;	

public interface BoardRepositoryInterface {
	void save(Board board) throws BoardMaxNumberReachedException;
	void update(Board board); 

	Board findById(int id) throws BoardNotFoundException;	
	Map<String, Board> findAll();
	
	void delete(Board board);		
	void deleteAll();
	
	int getNextId();
	void resetId();

	long count();
}
