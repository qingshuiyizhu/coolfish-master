package org.com.coolfish.lister.service;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "total-silent")
public class QueryListener {

    @Autowired
    private QueryMonthlyService queryMonthlyService;

    @Autowired
    private TotalSilentService totalSilentService;

    @RabbitHandler
    public void process(String message) {

        // 监听累计套沉默期队列
        totalSilentService.handle(message);
    }

}
