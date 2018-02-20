package com.zafatar.robopark.api.v1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zafatar.robopark.api.v1.exceptions.BoardMaxNumberReachedException;
import com.zafatar.robopark.api.v1.exceptions.BoardNotFoundException;
import com.zafatar.robopark.api.v1.model.Board;
import com.zafatar.robopark.api.v1.model.response.ApiBasicResponse;
import com.zafatar.robopark.api.v1.model.response.ApiResponse;
import com.zafatar.robopark.api.v1.repository.BoardRepository;

@RestController
@RequestMapping("/api/v1/boards")
public class BoardRestController {
	// private static final Logger log = LoggerFactory.getLogger(BoardRestController.class);

	private final BoardRepository boardRepository;

	@Autowired
	BoardRestController(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	/**
	 * This method creates a board with the given `width` and `height`.
	 * 
	 * @param board board attributes
	 * @return nothing, but the link of the board in the header
	 * @throws BoardMaxNumberReachedException
	 */
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<ApiResponse> createBoard(@RequestBody Board board) throws BoardMaxNumberReachedException {
		Board createdBoard = new Board(board.getWidth(), board.getHeight());
		this.boardRepository.save(createdBoard);

		// Set the new board as active board in the system.
		this.boardRepository.setActiveBoardId(createdBoard.getId());
		
		ApiResponse ar = new ApiResponse(HttpStatus.CREATED, "board created", createdBoard);
		return new ResponseEntity<ApiResponse>(ar, ar.getStatus());
	}

	/**
	 * This method returns the board with the given id.
	 * 
	 * @param id requested board id
	 * @return requested board
	 * @throws BoardNotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	ResponseEntity<ApiResponse> getBoard(@PathVariable int id) throws BoardNotFoundException {
		Board board = this.boardRepository.findById(id);
		
		ApiResponse ar = new ApiResponse(HttpStatus.OK, "board returned", board);
		return new ResponseEntity<ApiResponse>(ar, ar.getStatus());
	}

	/**
	 * TODO: This method action is commented since, deletion as feature, is not ready.
	 * 
	 * @param id board id to be removed.
	 * @return deleted board
	 * @throws BoardNotFoundException
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	ResponseEntity<?> deleteBoard(@PathVariable int id) throws BoardNotFoundException {
		Board board = this.boardRepository.findById(id);
		//this.boardRepository.delete(board);
		// TODO: Better deletion message
		return new ResponseEntity<Board>(board, HttpStatus.NO_CONTENT);
	}	
	
	/**
	 * This method resets the board. Use with caution. It removes all boards. 
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/reset")
	ResponseEntity<ApiBasicResponse> resetBoard() {
		this.boardRepository.deleteAll();
		this.boardRepository.resetId();
		
		ApiBasicResponse ar = new ApiBasicResponse(HttpStatus.NO_CONTENT, "all boards deleted");
		return new ResponseEntity<ApiBasicResponse>(ar, ar.getStatus()); 
	}
}
