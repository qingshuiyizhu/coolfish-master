package org.com.coolfish.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class  RedisConfiguration {
  	  @Value("${spring.redis.host}")
	  private String host;

	  @Value("${spring.redis.port}")
	  private int port;

	  @Value("${spring.redis.timeout}")
	  private int timeout;

	  @Value("${spring.redis.password}")
	  private String password; 

	  @Value("${spring.redis.database}")
	  private int database;

	  @Value("${spring.redis.pool.max-idle}")
	  private int maxIdle;

	  @Value("${spring.redis.pool.min-idle}") 
	  private int minIdle;

	  /**
	   * redis模板，存储关键字是字符串，值是Jdk序列化
	   * @Description:
	   * @param factory
	   * @return
	   */
	  @Bean
	  public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory) {
	      RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
	      redisTemplate.setConnectionFactory(factory);
	      RedisSerializer<String> redisSerializer = new StringRedisSerializer();
	      redisTemplate.setKeySerializer(redisSerializer);
	      redisTemplate.setHashKeySerializer(redisSerializer);
	      //JdkSerializationRedisSerializer序列化方式;
	      JdkSerializationRedisSerializer jdkRedisSerializer=new JdkSerializationRedisSerializer();
	      redisTemplate.setValueSerializer(jdkRedisSerializer);
	      redisTemplate.setHashValueSerializer(jdkRedisSerializer);
	      redisTemplate.afterPropertiesSet();
	      return redisTemplate; 
	  }
 
}
