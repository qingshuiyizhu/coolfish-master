package org.com.coolfish.ctccmonthly;

import org.com.coolfish.cache.AccountCache;
import org.com.coolfish.entity.CTCCOperator;
import org.com.coolfish.message.MsisdnMessage;
import org.com.coolfish.service.AccountCacheService;
import org.com.coolfish.service.CTCCRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

//电信月套餐队列
@Component
@RabbitListener(queues = "ctcc-monthly")
public class MQReceiver {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CTCCRestService ctccRestService;

    @Autowired
    private AccountCacheService accountCacheService;

    // 标识 不关联账户密钥表
    private final static int NOZID = 0;

    // 监听电信月套餐队列
    @RabbitHandler
    public void process(String message) {
        // 将String 转为MsisdnMessage对象
        logger.info("电信月套餐队列(ctcc-monthly)获取数据[{}]", message);
        MsisdnMessage msisdnMessage = JSON.parseObject(message, MsisdnMessage.class);
        // 传到消息处理方法
        long startTime = System.currentTimeMillis();
        logger.info("电信号码[{}]开始执行流量监控, 时间:{}", msisdnMessage.getTel() , startTime);
        handleMessage(msisdnMessage);
        long endTime = System.currentTimeMillis();
        logger.info("电信号码[{}]结束执行流量监控, 时间:{}, 耗时:{}ms", msisdnMessage.getTel(), endTime,(endTime - startTime));
  
    }

    // 处理监听到的数据
    public void handleMessage(MsisdnMessage msisdnMessage) {
        // 请求电信API的需要的密钥对象
        CTCCOperator operator = null;
        if (NOZID == msisdnMessage.getZid()) {
            // 账号信息以json格式文本存在Operator的text字段上
            Integer id = msisdnMessage.getOperatorid();
            operator = AccountCache.getInstance().getOperator((long) id);
            if (null == operator) {
                accountCacheService.getOneOperator((long) id);
                operator = AccountCache.getInstance().getOperator((long) id);
            }
        } else {
            // 关联独立的账号表
            Integer id = msisdnMessage.getZid();
            operator = AccountCache.getInstance().getAccount((long) id);
            if (null == operator) {
                accountCacheService.getOneAccount((long) id);
                operator = AccountCache.getInstance().getAccount((long) id);
            }
        }

        if (null == operator) {
            logger.error("电信号码[{}]没有找到对应供应商的账号信息", msisdnMessage.getTel());
        } else {
            logger.info("[{}]调用电信流量监控服务,运营商账号信息：{}", msisdnMessage.getTel(), operator.toString());
            // 调用流量监控服务
            ctccRestService.handle(operator, msisdnMessage);
        }
    }

}
