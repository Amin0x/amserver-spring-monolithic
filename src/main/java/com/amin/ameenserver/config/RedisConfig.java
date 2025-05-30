package com.amin.ameenserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {
    public static final String DRIVER_LOCATION_KEY = "amin:driver:locations";

    //Creating Connection with Redis
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        //return new LettuceConnectionFactory(new RedisStandaloneConfiguration("server", 6379));
        return new LettuceConnectionFactory();
    }

    //@Bean
    //JedisConnectionFactory jedisConnectionFactory() {
        //final JedisConnectionFactory jcf;
        //jcf = new JedisConnectionFactory();
        //return jcf;
    //}

    //Creating RedisTemplate for Entity 'Employee'
    @Bean
    public RedisTemplate<String, Object> redisTemplate(){
        RedisTemplate<String, Object> empTemplate = new RedisTemplate<>();
        empTemplate.setConnectionFactory(redisConnectionFactory());
        return empTemplate;
    }
}
