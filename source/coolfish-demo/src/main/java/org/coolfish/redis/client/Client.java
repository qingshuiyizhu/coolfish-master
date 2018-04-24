package org.coolfish.redis.client;

import java.util.ArrayList;
import java.util.List;

import org.coolfish.redis.entity.User;
import org.coolfish.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class Client {
	@Autowired
    RedisService redisService;
 
    public void get() {
     
        User user = new User();
        user.setName("wangjianfeng");
        user.setAge(22);
        redisService.add("userByName:" + user.getName(), user, 10L);
        List<User> list = new ArrayList<>();
        list.add(user);
        redisService.add("list", list, 10L);
        User user1 =new User();
        user1 = redisService.get("userByName:wangjianfeng");
        System.out.println(user1.getAge());
        Assert.notNull(user1, "user is null");
        List<User> list2 = redisService.getUserList("list");
        Assert.notNull(list2, "list is null");
    }
}
