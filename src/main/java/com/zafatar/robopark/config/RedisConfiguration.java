package com.zafatar.robopark.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis configuration class based on the properties file with 3 elements: 
 * redis.host : hostname or IP of the Redis server.
 * redis.port : port of the Redis server. 
 * redis.database : the database number where data will be stored on Redis.
 * 
 * @author zafatar
 *
 */
@Configuration
@EnableRedisRepositories
@PropertySource("classpath:application.properties")
public class RedisConfiguration {

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")	
    private int redisPort;
    
    @Value("${redis.database}")
    private int redisDatabase;
   
    @Bean
    RedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
        jedisConFactory.setHostName(this.redisHost);
        jedisConFactory.setPort(this.redisPort);
        jedisConFactory.setDatabase(this.redisDatabase);
        return jedisConFactory;    	
    }
    
    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<String, String>();
        template.setConnectionFactory(jedisConnectionFactory());
        // For better/readable serialization.
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        
        return template;
    }
}