package org.coolfish.redis.repository;

import java.util.List;

import org.coolfish.redis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/*public interface UserRepository extends JpaRepository<User, Integer> {
	@Query("select new User(u.id,u.name,u.age) from User u where u.name = :name")
  	List<User> find_name(@Param("name") String name); 
}
*/