package com.zafatar.robopark.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
@PropertySource("classpath:application.properties")
public class BoardRepositoryConfiguration {
	 @Value("${boards.maxNumber}")
	 private int maxNumber;

	 @Bean
	 public int getMaxNumber() {
		 return this.maxNumber;
	 }
}
