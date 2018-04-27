package org.com.coolfish.cmcc.monthly.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisService {
	@Autowired
	StringRedisTemplate stringRedisTemplate;

	public void set(String key, String value) {
		stringRedisTemplate.opsForValue().set(key, value, 10L, TimeUnit.MINUTES);
	}

	public String get(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	 
	}
 	 
	public void delete(String key) {
		stringRedisTemplate.opsForValue().getOperations().delete(key);
	}

	public void pushAll(String [] strtels) {
	 	stringRedisTemplate.opsForList().leftPushAll("cmccTels",strtels); 
 		
	}

}
