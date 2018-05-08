package org.com.coolfish.lister.service;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "query-monthly")
public class QueryListener {

    @Autowired
    private QueryMonthlyService queryMonthlyService;

    @RabbitHandler
    public void process(String message) {

        // 监听月套餐队列
         queryMonthlyService.handle(message);
    }

}
