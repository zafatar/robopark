package com.zafatar.robopark.api.v1.repository;

import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.stereotype.Repository;

import com.zafatar.robopark.api.v1.exceptions.BoardMaxNumberReachedException;
import com.zafatar.robopark.api.v1.exceptions.BoardNotFoundException;
import com.zafatar.robopark.api.v1.model.Board;

/**
 * This is the representation of Board Repository based on Redis Template. This
 * library allows to reach the resources and retrieve or save them properly.
 * 
 * The board repository keeps only given number of Board in the stash. This can be 
 * set in the config file.
 * 
 * @author zafatar
 *
 */
@Repository
public class BoardRepository implements BoardRepositoryInterface {
	private static final Logger log = LoggerFactory.getLogger(BoardRepository.class);
	private static final String KEY = "boards";     // Redis key prefix for repo

	private RedisTemplate<String, String> redisTemplate;
	// The boards will be kept under a hash;
	// Key / Field => Value
	// boards / Board:<id> => Board
	private HashOperations<String, String, Board> hashOperations;
	private int activeBoardId = 1 ; // By default, first board is active.
	private int maxNumber;          // Max number of Board in the repo -from config.
	
	protected BoardRepository() {
	} // Visible constructor.

	@Autowired
	private BoardRepository(RedisTemplate<String, String> redisTemplate, int maxNumber) {
		this.redisTemplate = redisTemplate;
		this.maxNumber = maxNumber;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}
	
	@Override
	public void save(Board board) throws BoardMaxNumberReachedException {
		if ( this.count() >= this.maxNumber )
			throw new BoardMaxNumberReachedException();

		// Board sequence.
		int boardId = this.getNextId();
		board.setId(boardId);
		
		hashOperations.put(KEY, uniqueBoardKey(board.getId()), board);
		log.info("Board number #" + board.getId() + " saved.");
	}

	@Override
	public void update(Board board) {
		hashOperations.put(KEY, uniqueBoardKey(board.getId()), board);
	}

	@Override
	public Board findById(int id) throws BoardNotFoundException {
		Board board = (Board) hashOperations.get(KEY, uniqueBoardKey(id));
		log.debug("Board #" + id + " found.");
		
		if (board == null)
			throw new BoardNotFoundException(id);

		return board;
	}

	@Override
	public Map<String, Board> findAll() {
		return hashOperations.entries(KEY);
	}

	@Override
	public void delete(Board board) {
		hashOperations.delete(KEY, uniqueBoardKey(board.getId()));
	}

	@Override
	public void deleteAll() {
		Set<String> boardKeys = hashOperations.keys(KEY);
		for (String boardKey : boardKeys) {
		    hashOperations.delete(KEY, boardKey);
		} 
	}
	
	@Override
	public long count() {
		return hashOperations.size(KEY);
	}

	/** 
	 * This method returns the next/new id from the sequence for the new board.
	 */
	@Override
	public int getNextId() {
		RedisAtomicInteger rai = new RedisAtomicInteger(KEY + ":sequence", this.redisTemplate.getConnectionFactory());
		return rai.incrementAndGet();
	}
		
	/**
	 * This method resets the board sequence.
	 */
	@Override
	public void resetId() {
		RedisAtomicInteger rai = new RedisAtomicInteger(KEY + ":sequence", this.redisTemplate.getConnectionFactory());
		rai.set(0);
	}
	
	/**
	 * Since this is a Redis-based repository, we need a Redis key in string
	 * format. We could use casting for Board id to String and keep in this way
	 * too. But in this way it's much clean and readable.
	 * 
	 * Format is Board:<id>
	 */
	private String uniqueBoardKey(int id) {
		return Board.class.getSimpleName() + ":" + id;
	}

	public int getActiveBoardId() {
		return activeBoardId;
	}

	public void setActiveBoardId(int activeBoardId) {
		this.activeBoardId = activeBoardId;
	}
}