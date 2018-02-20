package com.zafatar.robopark.api.v1.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;

@Entity
@RedisHash("boards")
public class Board implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int			  id;
	private int 			  width;
	private int 			  height;
	private List<Integer> robots;
	
	protected Board() {}

	public Board(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);
	}
	
	/**
	 * Getter and setters
	 */
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public List<Integer> getRobots() {
		return robots;
	}
	
	public void setRobots(List<Integer> robots) {
		this.robots = robots;
	}
	
	public void addRobot(Robot robot) {
		
	}
	
	@Override
	public String toString() {
		return String.format("Board ID#%s W: %s H: %s", this.id, this.width, this.height); 
	} 
}
