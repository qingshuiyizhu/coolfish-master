package org.com.coolfish.cmcc.monthly.service;

import org.com.coolfish.common.database.service.ComDBService;
import org.com.coolfish.common.message.MsisdnMessage;
import org.com.coolfish.common.model.CMCCOperator;
import org.com.coolfish.common.util.DecimalTools;
import org.com.coolfish.common.webinterface.service.CMCCRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @author LINGHUI
 * @desc 中国移动http 请求API
 */
@Service
public class CMCCRestService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CMCCRequestService requestService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private ComDBService databaseService;

    public void handle(CMCCOperator operator, MsisdnMessage message) {
        // 1. 查询号码使用量
        String nowApiflow = requestService.queryTraffic(operator, message);
        // 2.1使用量为null 查询失败
        if (nowApiflow == null) {
            logger.warn("移动号码[{}]查询使用量失败，传回队列cmcc-monthly85", message.getTel());
            // 流程查询失败，传回队列cmcc-monthly85
            rabbitTemplate.convertAndSend("cmcc-monthly85", JSON.toJSONString(message).toString());
        } else {
            // 使用量 <=0
            double subResult = DecimalTools.sub(nowApiflow, String.valueOf(1024));
            if (DecimalTools.compareTo(subResult, 0d) > -1) {
                logger.info("处于沉默期的移动号码[{}]已使用量[{} kb],进行套餐包激活", nowApiflow);
                flashSlientStatus(message.getTel());
            }
        }

    }

    public void flashSlientStatus(String tel) {
        databaseService.flashSlientStatus(tel);
    }
}
