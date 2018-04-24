package org.coolfish.redis.service;

/*
import java.util.List;

import org.coolfish.redis.entity.User;
import org.coolfish.redis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
	 @Autowired
	private UserRepository repository;

	@Transactional
	public void delete(Integer id) {
		repository.delete(id);
	}

	@Transactional(readOnly = true)
	public User get(Integer id) {
		return repository.findOne(id);
	}

	@Transactional
	public void save(User user) {
		repository.saveAndFlush(user);
	}

	 
	@Transactional(readOnly = true)
	public List<User> findAll() {
		return repository.findAll();
	}
	 
	@Transactional(readOnly = true)
	public List<User> find_name(String name) {
		return repository.find_name(name);
	}

}
*/