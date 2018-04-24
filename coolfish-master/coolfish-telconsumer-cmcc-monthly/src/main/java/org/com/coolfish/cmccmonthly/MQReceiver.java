package org.com.coolfish.cmccmonthly;

import org.com.coolfish.entity.CMCCOperator;
import org.com.coolfish.message.MsisdnMessage;
import org.com.coolfish.service.CMCCRestService;
import org.com.coolfish.service.FindOperatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

//移动月套餐队列
@Component
@RabbitListener(queues = "cmcc-monthly")
public class MQReceiver {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CMCCRestService cmccRestService;

    @Autowired
    private FindOperatorService findOperatorService;

    // 不关联账户密钥表
    private final static int NOZID = 0;

    // 监听移动月套餐队列
    @RabbitHandler
    public void process(String message) {
        // 将String 转为MsisdnMessage对象
        logger.info("移动月套餐队列(cmcc-monthly)获取数据[{}]" , message);
        MsisdnMessage msisdnMessage = JSON.parseObject(message, MsisdnMessage.class);
        handleMessage(msisdnMessage);
    }

    // 处理监听到的数据
    public void handleMessage(MsisdnMessage msisdnMessage) {
        // 请求移动API的需要的密钥对象
        CMCCOperator operator = null;
        if (NOZID >= msisdnMessage.getZid()) {
            // 账号信息以json格式文本存在text字段上
            operator = findOperatorService.getoperator(msisdnMessage.getOperatorid().longValue());
        } else {
            // 关联独立的账号表
            operator = findOperatorService.getaccount(msisdnMessage.getZid().longValue());
        }
        
        if (null == operator) {
            logger.warn("没有找到移动供应商的账号信息, 号码:{}", msisdnMessage.getTel());
        } else {
            logger.info(msisdnMessage.getTel() + "调用移动流量监控服务,运营商账号信息：{}", operator.toString());
            // 调用流量监控服务
            cmccRestService.handle(operator, msisdnMessage);
        }

    }
}
