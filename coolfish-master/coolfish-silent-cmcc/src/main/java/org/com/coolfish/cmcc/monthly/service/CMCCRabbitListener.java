package org.com.coolfish.cmcc.monthly.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.com.coolfish.common.cache.CMCCAccountCache;
import org.com.coolfish.common.message.MsisdnMessage;
import org.com.coolfish.common.model.CMCCOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

//移动月套餐队列
@Component
@RabbitListener(queues = "cmcc-silent")
public class CMCCRabbitListener {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CMCCRestService cmccRestService;

    @Autowired
    private AccountCacheService accountCacheService;

    // 不关联账户密钥表
    private final static int NOZID = 0;
    
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 监听移动月套餐队列
    @RabbitHandler
    public void process(String message) {
        // 将String 转为MsisdnMessage对象
        logger.info("移动沉默期队列(cmcc-silent)获取数据[{}]", message);
        MsisdnMessage msisdnMessage = JSON.parseObject(message, MsisdnMessage.class);
        long startTime = System.currentTimeMillis();
        logger.info("移动号码[{}]开始执行沉默期处理, 开始执行时间:{}, 时间戳:{}", msisdnMessage.getTel(), sdf.format(new Date(startTime)), startTime);
        handleMessage(msisdnMessage);
        long endTime = System.currentTimeMillis();
        logger.info("移动号码[{}]结束执行沉默期处理, 结束执行时间:{}, 时间戳:{}, 耗时:{}ms", msisdnMessage.getTel(), sdf.format(new Date(endTime)), endTime,
                (endTime - startTime));
    }

    // 处理监听到的数据
    public void handleMessage(MsisdnMessage msisdnMessage) {
        // 请求移动API的需要的密钥对象
        CMCCOperator operator = null;
        if (NOZID >= msisdnMessage.getZid()) {
            // 账号信息以json格式文本存在text字段上
            Integer id = msisdnMessage.getOperatorid();
            operator = CMCCAccountCache.getInstance().getOperator(id);
            if (null == operator) {
                accountCacheService.getOneOperator(id);
                operator = CMCCAccountCache.getInstance().getOperator(id);
            }
        } else {
            // 关联独立的账号表
            Integer id = msisdnMessage.getZid();
            operator = CMCCAccountCache.getInstance().getAccount(id);
            if (null == operator) {
                accountCacheService.getOneAccount(id);
                operator = CMCCAccountCache.getInstance().getAccount(id);
            }
        }
        if (null == operator) {
            logger.warn("没有找到移动供应商的账号信息, 号码:{}", msisdnMessage.getTel());
        } else {
            logger.info(msisdnMessage.getTel() + "调用移动沉默期处理服务,运营商账号信息：{}", operator.toString());
            cmccRestService.handle(operator, msisdnMessage);
        }

    }
}
