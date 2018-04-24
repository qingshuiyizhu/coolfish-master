package org.coolfish.redis.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.coolfish.redis.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

	public void add(String key, User user, Long time) {
		Gson gson = new Gson();
		stringRedisTemplate.opsForValue().set(key, gson.toJson(user), time, TimeUnit.MINUTES);
	}

	public void add(String key, List<User> users, Long time) {
		Gson gson = new Gson();
		String src = gson.toJson(users);
		stringRedisTemplate.opsForValue().set(key, src, time, TimeUnit.MINUTES);
	}

	public User get(String key) {
		String source = stringRedisTemplate.opsForValue().get(key);
		if (!StringUtils.isEmpty(source)) {
			return new Gson().fromJson(source, User.class);
		}
		return null;
	}

	public List<User> getUserList(String key) {
		String source = stringRedisTemplate.opsForValue().get(key);
		if (!StringUtils.isEmpty(source)) {
			return new Gson().fromJson(source, new TypeToken<List<User>>() {
			}.getType());
		}
		return null;
	}

	public void delete(String key) {
		stringRedisTemplate.opsForValue().getOperations().delete(key);
	}

	public void pushAll(String [] strtels) {
	 	stringRedisTemplate.opsForList().leftPushAll("cmccTels",strtels); 
 		
	}

}
