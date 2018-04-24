package org.com.coolfish.cmcc.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisService {
	@Autowired
	StringRedisTemplate stringRedisTemplate;

	public void addTel(String key, String operatorid, Long time) {
		stringRedisTemplate.opsForValue().set(key, operatorid, time, TimeUnit.MINUTES);
	}

	public String getTel(String key) {
		return stringRedisTemplate.opsForValue().get(key);

	}

	public List<String> getTelAll() {
	 	System.out.println(stringRedisTemplate.opsForList().range("cmccTels",0,-1));
		return stringRedisTemplate.opsForList().range("cmccTels",0,-1);
	 	}

	public void delete(String key) {
		stringRedisTemplate.opsForValue().getOperations().delete(key);
	}

}
