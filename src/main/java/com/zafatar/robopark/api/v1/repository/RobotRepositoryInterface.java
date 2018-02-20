package com.zafatar.robopark.api.v1.repository;

import java.util.Map;

import com.zafatar.robopark.api.v1.exceptions.RobotNotFoundException;
import com.zafatar.robopark.api.v1.model.Robot;

public interface RobotRepositoryInterface {
	void save(Robot robot);
	void update(Robot robot); 

	Robot findById(int id) throws RobotNotFoundException;	
	Map<String, Robot> findAll();
	
	void delete(Robot Robot);	
	void deleteAll();
	
	int getNextId();
	void resetId();

	long count();
}
