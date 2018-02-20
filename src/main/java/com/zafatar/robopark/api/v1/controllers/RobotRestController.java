package com.zafatar.robopark.api.v1.controllers;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zafatar.robopark.api.v1.exceptions.BoardNotFoundException;
import com.zafatar.robopark.api.v1.exceptions.RobotMoveNotPossibleException;
import com.zafatar.robopark.api.v1.exceptions.RobotNotFoundException;
import com.zafatar.robopark.api.v1.exceptions.RobotPlaceOutOfBorderException;
import com.zafatar.robopark.api.v1.model.Board;
import com.zafatar.robopark.api.v1.model.Direction;
import com.zafatar.robopark.api.v1.model.Robot;
import com.zafatar.robopark.api.v1.model.response.ApiBasicResponse;
import com.zafatar.robopark.api.v1.model.response.ApiResponse;
import com.zafatar.robopark.api.v1.repository.BoardRepository;
import com.zafatar.robopark.api.v1.repository.RobotRepository;

@RestController
@RequestMapping("/api/v1/robots")
public class RobotRestController {
	private final BoardRepository boardRepository;
	private final RobotRepository robotRepository;
	
	@Autowired
	RobotRestController(BoardRepository boardRepository, RobotRepository robotRepository) {
		this.boardRepository = boardRepository;
		this.robotRepository = robotRepository;
	}
	
	/**
	 * This method returns all the robots defined in the system. 
	 * 
	 * @return response containing the list of robots.
	 */
	@RequestMapping(method = RequestMethod.GET)
	ResponseEntity<ApiResponse> getAllRobots() {
		Map<String, Robot> robotsAsHash = this.robotRepository.findAll();
		Collection<Robot> robotsAsList = robotsAsHash.values();
	
		ApiResponse ar = new ApiResponse(HttpStatus.OK, "robot list returned", robotsAsList);
		return new ResponseEntity<ApiResponse>(ar, ar.getStatus());
	}
	
	/**
	 * This method create a robot in the system by reading the request body.
	 * 
	 * @param robot a temp robot created from request body.
	 * @return response containing the created robot.
	 */
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<ApiResponse> createRobot(@RequestBody Robot robot) {
		Robot createdRobot = new Robot();
		createdRobot.setName(robot.getName());
		this.robotRepository.save(createdRobot);
		
		ApiResponse ar = new ApiResponse(HttpStatus.CREATED, "robot created", createdRobot);
		return new ResponseEntity<ApiResponse>(ar, ar.getStatus());
	}
	
	/**
	 * This method places the robot to the given position and direction.
	 * 
	 * @param id id of the robot to be placed
	 * @param updatedRobot partial robot instance with location and face.
	 * @return response containing the placed robot.
	 * @throws RobotNotFoundException
	 * @throws BoardNotFoundException
	 * @throws RobotPlaceOutOfBorderException 
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/{id}")
	ResponseEntity<ApiResponse> placeRobot(@PathVariable int id, @RequestBody Robot updatedRobot) 
		throws RobotNotFoundException, BoardNotFoundException, RobotPlaceOutOfBorderException {
		// Board in which the robot to be placed.
		Board board = this.boardRepository.findById(this.boardRepository.getActiveBoardId());
		Robot robot = (Robot) this.robotRepository.findById(id);

		if (updatedRobot.getLocation().getX() + 1 >= board.getWidth() || 
			updatedRobot.getLocation().getY() + 1 >= board.getHeight() ) {
			throw new RobotPlaceOutOfBorderException(robot.getId());
		}
			
		robot.setLocation(updatedRobot.getLocation());
		robot.setFace(updatedRobot.getFace());
		this.robotRepository.update(robot);
		
		// Get active board here and add this robot to the board.
		
		board.setRobots(Arrays.asList(robot.getId()));
		this.boardRepository.update(board);
		
		ApiResponse ar = new ApiResponse(HttpStatus.OK, "robot placed", robot);
		return new ResponseEntity<ApiResponse>(ar, ar.getStatus()); 
	}
	
	/**
	 * This method returns the robot requested by its id.
	 * 
	 * @param id of the robot.
	 * @return response containing the requested robot.
	 * @throws RobotNotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	ResponseEntity<ApiResponse> getRobot(@PathVariable int id) throws RobotNotFoundException {
		Robot robot = this.robotRepository.findById(id);	
		
		ApiResponse ar = new ApiResponse(HttpStatus.OK, "robot returned", robot);
		return new ResponseEntity<ApiResponse>(ar, ar.getStatus()); 
	}
	
	/**
	 * This method moves the robot.
	 * 
	 * @param id of the robot to be moved.
	 * @return response containing the moved robot.
	 * @throws RobotNotFoundException
	 * @throws BoardNotFoundException
	 * @throws RobotMoveNotPossibleException
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/move")
	ResponseEntity<ApiResponse> moveRobot(@PathVariable int id) throws RobotNotFoundException, BoardNotFoundException, RobotMoveNotPossibleException {
		Robot robot = this.robotRepository.findById(id);
		Board board = this.boardRepository.findById(this.boardRepository.getActiveBoardId());
		Boolean moved = Boolean.FALSE;
		
		switch(robot.getFace()) {
			case NORTH: 
				if (robot.getLocation().getY() + 1 < board.getHeight() ) {
					robot.setY(robot.getLocation().getY() + 1);
					moved = Boolean.TRUE;
				}
				break;
			case WEST:				
				if (robot.getLocation().getX() - 1 >= 0 ) {
					robot.setX(robot.getLocation().getX() - 1);
					moved = Boolean.TRUE;
				}
				break;
			case SOUTH: 				
				if (robot.getLocation().getY() - 1 >= 0 ) {
					robot.setY(robot.getLocation().getY() - 1);
					moved = Boolean.TRUE;
				}
				break;
			case EAST:  				
				if (robot.getLocation().getX() + 1 < board.getWidth() ) {
					robot.setY(robot.getLocation().getX() + 1);
					moved = Boolean.TRUE;
				}
				break;						
			default:
				break;
		}
		
		if (moved)
			this.robotRepository.update(robot);
		else 
			throw new RobotMoveNotPossibleException("Move not possible for robot");
		
		ApiResponse ar = new ApiResponse(HttpStatus.OK, "robot moved", robot);
		return new ResponseEntity<ApiResponse>(ar, ar.getStatus()); 
	}

	/**
	 * This method make the robot turn to left. 
	 * 
	 * @param id of the robot to turn left.
	 * @return response containing the placed robot.
	 * @throws RobotNotFoundException
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/left")
	ResponseEntity<ApiResponse> turnRobotLeft(@PathVariable int id) throws RobotNotFoundException {
		Robot robot = this.robotRepository.findById(id);
		
		switch(robot.getFace()) {
			case NORTH: robot.setFace(Direction.WEST);
					    break;
			case WEST:  robot.setFace(Direction.SOUTH);
						break;
			case SOUTH: robot.setFace(Direction.EAST);
						break;
			case EAST:  robot.setFace(Direction.NORTH);
		    				break;						
			default:
				break;
		}
		this.robotRepository.update(robot);
		
		ApiResponse ar = new ApiResponse(HttpStatus.OK, "robot turned to left", robot);
		return new ResponseEntity<ApiResponse>(ar, ar.getStatus()); 
	}
	
	/**
	 * This method make the robot turn to right. 
	 * 
	 * @param id of the robot to turn right.
	 * @return response containing the placed robot.
	 * @throws RobotNotFoundException
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/right")
	ResponseEntity<ApiResponse> turnRobotRight(@PathVariable int id) throws RobotNotFoundException {
		Robot robot = this.robotRepository.findById(id);
		
		switch(robot.getFace()) {
			case NORTH: robot.setFace(Direction.EAST);
					    break;
			case EAST:  robot.setFace(Direction.SOUTH);
						break;
			case SOUTH: robot.setFace(Direction.WEST);
						break;
			case WEST:  robot.setFace(Direction.NORTH);
		    				break;						
			default:
				break;
		}
		this.robotRepository.update(robot);
		
		ApiResponse ar = new ApiResponse(HttpStatus.OK, "robot turned to right", robot);
		return new ResponseEntity<ApiResponse>(ar, ar.getStatus()); 
	}
	
	/**
	 * This method calls a report method on the given robot.
	 * 
	 * @param id of the robot.
	 * @return response containing the placed robot.
	 * @throws RobotNotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/report")
	ResponseEntity<?> report(@PathVariable int id) throws RobotNotFoundException {
		Robot robot = this.robotRepository.findById(id);
		
		ApiResponse ar = new ApiResponse(HttpStatus.OK, robot.printReport(), robot);
		return new ResponseEntity<ApiResponse>(ar, ar.getStatus()); 
	}
	
	/**
	 * This method resets the robots. Use with caution. It removes all robots. 
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/reset")
	ResponseEntity<ApiBasicResponse> resetRobots() {
		this.robotRepository.deleteAll();
		
		this.robotRepository.resetId();
		
		ApiBasicResponse ar = new ApiBasicResponse(HttpStatus.NO_CONTENT, "all robots deleted");
		return new ResponseEntity<ApiBasicResponse>(ar, ar.getStatus()); 
	}
}
