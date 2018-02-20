package com.zafatar.robopark.api.v1.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.data.geo.Point;

@Entity
public class Robot implements Serializable {
	private static final long serialVersionUID = 42L;
	
	@Id
	private int 			id;
	private String 		name;
	private Point 		location;
	private Direction 	face;
	
	public Robot(){}

	public Robot(int x, int y, Direction face) {
		this.location = new Point(x,y);
		this.face = face;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}
	
	public Direction getFace() {
		return face;
	}
	
	public void setFace(Direction face) {
		this.face = face;
	}
	
	public void setX(double x) {
		this.location = new Point(x, this.location.getY());
	}

	public void setY(double d) {
		this.location = new Point(this.location.getX(), d);
	}
	
	public String printReport() {
		return String.format("Robot ID#%s Output: %s, %s, %s", this.id, 
															  this.location.getX(), 
															  this.location.getY(),
															  this.face.toString()); 
	}
}
