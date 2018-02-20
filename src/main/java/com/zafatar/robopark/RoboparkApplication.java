package com.zafatar.robopark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class of Robopark application.
 * 
 * @author zafatar
 *
 */
@SpringBootApplication
public class RoboparkApplication {
	private static final Logger log = LoggerFactory.getLogger(RoboparkApplication.class);
	
	public static void main(String[] args) {
		log.info("Robopark application started.");
		SpringApplication.run(RoboparkApplication.class, args);
	}
}
