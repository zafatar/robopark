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

import com.zafatar.robopark.api.v1.exceptions.RobotNotFoundException;
import com.zafatar.robopark.api.v1.model.Robot;

/**
 * This is the representation of Robot Repository based on Redis Template. This
 * library allows to reach the resources and retrieve or save them properly.
 * 
 * @author zafatar
 *
 */
@Repository
public class RobotRepository implements RobotRepositoryInterface {
	private static final Logger log = LoggerFactory.getLogger(RobotRepository.class);
	private static final String KEY = "robots";    // Redis key prefix for repo

	private RedisTemplate<String, String> redisTemplate;
	// The robots will be kept under a hash;
	// Key / Field => Value
	// Robots / Robot:<id> => Robot
	private HashOperations<String, String, Robot> hashOperations;

	protected RobotRepository() {
	} // Visible constructor.

	@Autowired
	private RobotRepository(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}

	@Override
	public void save(Robot robot) {
		// get sequence here.
		int boardId = this.getNextId();
		robot.setId(boardId);
		
		hashOperations.put(KEY, uniqueRobotKey(robot.getId()), robot);
		log.info("Robot number #" + robot.getId() + " saved.");
	}

	@Override
	public void update(Robot robot) {
		hashOperations.put(KEY, uniqueRobotKey(robot.getId()), robot);
		log.info("Robot #" + robot.getId() + " updated: " + robot.getLocation().getX() + " x " + robot.getLocation().getY() );
	}

	@Override
	public Robot findById(int id) throws RobotNotFoundException {
		Robot robot = (Robot) hashOperations.get(KEY, uniqueRobotKey(id));
		log.debug("Robot #" + id + " found.");
		
		if (robot == null)
			throw new RobotNotFoundException(id);

		return robot;
	}

	@Override
	public Map<String, Robot> findAll() {
		return hashOperations.entries(KEY);
	}

	@Override
	public void delete(Robot robot) {
		hashOperations.delete(KEY, robot.getId());
	}
	
	@Override
	public void deleteAll() {
		Set<String> robotIds = hashOperations.keys(KEY);
		for (String robotId : robotIds) {
		    hashOperations.delete(KEY, robotId);
		} 
	}
	
	/** 
	 * This method returns the next/new id from the sequence for the new robot.
	 */
	@Override
	public int getNextId() {
		RedisAtomicInteger rai = new RedisAtomicInteger(KEY + ":sequence", this.redisTemplate.getConnectionFactory());
		return rai.incrementAndGet();
	}
	
	/**
	 * This method resets the robot sequence.
	 */
	@Override
	public void resetId() {
		RedisAtomicInteger rai = new RedisAtomicInteger(KEY + ":sequence", this.redisTemplate.getConnectionFactory());
		rai.set(0);
	}
	
	@Override
	public long count() {
		return hashOperations.size(KEY);
	}


	
	/**
	 * Since this is a redis-based repository, we need a redis key in string
	 * format. We could use casting for Board id to String and keep in this way
	 * too.
	 */
	private String uniqueRobotKey(int id) {
		return Robot.class.getSimpleName() + ":" + id;
	}
}