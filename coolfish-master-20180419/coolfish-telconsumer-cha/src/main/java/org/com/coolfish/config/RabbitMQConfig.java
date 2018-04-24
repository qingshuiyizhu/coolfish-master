package org.com.coolfish.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
@Configuration
public class RabbitMQConfig {
   // @Value("${coolfish.rabbitmq.queue}")
  /*  private String queueName=""; 
     @Bean
    public Queue Queue() {
        return new Queue("cmcc-monthly");
    } */
    
    
}
