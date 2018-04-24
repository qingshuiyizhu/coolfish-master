package org.coolfish.redis.service;

import java.util.List;

import org.coolfish.redis.entity.KuyuCard;
import org.coolfish.redis.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainService {
	/*
	 * @Autowired private UserService service;
	 */
	@Autowired
	private KuyuCardService service;
	@Autowired
    RedisService redisService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public void start() {
		long startTime = System.currentTimeMillis();    //获取开始时间
		logger.info("\n----------------------------开始拿数据");
		List<KuyuCard> list = service.findInA(137);
		long endTime = System.currentTimeMillis();    //获取结束时间
		logger.info("\n----------------------------拿到数据条数为："+list.size());
		logger.info("\n程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
		logger.info("\n----------------开始缓存到Redis--------------------");
	 	String[] tels = new String[list.size()] ;
	 	for (int i=0;i<list.size();i++) {
	 		tels[i]=list.get(i).getTel();
	 		logger.info("\n缓存数据："+tels[i]);
	 		//存储到Redis
		    //redisService.addTel(kuyuCard.getTel(), String.valueOf(kuyuCard.getOperatorid()), 10L);
		}
	 	redisService.pushAll(tels);
 	     System.out.println("\n-------------------------------------------------end");
 	      System.exit(0);;
	 }

	public void start1() {
		System.out.println("------------------------111");
		User user = new User();
		user.setAge(9);
		user.setName("張三");
		// service.save(user);
		// System.out.println(service.find_name("張三").get(0).getAge());

	}
}
