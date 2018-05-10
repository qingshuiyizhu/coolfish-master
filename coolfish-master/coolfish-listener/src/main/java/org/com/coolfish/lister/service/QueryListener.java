package org.com.coolfish.lister.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.com.coolfish.common.message.MsisdnMessage;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
@RabbitListener(queues = "fastquery-monthly")
public class QueryListener {
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private QueryMonthlyService queryMonthlyService;

    @Autowired
    private TotalSilentService totalSilentService;

    @RabbitHandler
    public void process(String message) {
        
        
        
        
        handle(message ,"月套餐流量状态快速查询队列(fastquery-monthly)");
    }

    
    public void handle(String message,String queueName) {
        // 将String 转为MsisdnMessage对象
        log.info("{}获取数据[{}]", queueName,message);
        MsisdnMessage msisdnMessage = JSON.parseObject(message, MsisdnMessage.class);
        long startTime = System.currentTimeMillis();
        log.info("物联网卡ID[{}]开始执行流量状态查询, 开始执行时间:{}, 时间戳:{}", msisdnMessage.getCardid(),
                sdf.format(new Date(startTime)), startTime);
         
        queryMonthlyService.handleMessage(msisdnMessage);
        
        
        long endTime = System.currentTimeMillis();
        log.info("物联网卡ID[{}]结束执行流量状态查询, 结束执行时间:{}, 时间戳:{}, 耗时:{}ms", msisdnMessage.getCardid(),
                sdf.format(new Date(endTime)), endTime, (endTime - startTime));
    }

}
