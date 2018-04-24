package org.com.coolfish.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class MQSendService {
    @Autowired
    private AmqpTemplate rabbitTemplate;
    
    public void send(String sendMsg) {
         this.rabbitTemplate.convertAndSend("helloQueue", sendMsg);
    }
    
  /*  public void send() {
        String sendMsg = "hello1 " + new Date();
        System.out.println("Sender1 : " + sendMsg);
        this.rabbitTemplate.convertAndSend("helloQueue", sendMsg);
    }*/
    
}
