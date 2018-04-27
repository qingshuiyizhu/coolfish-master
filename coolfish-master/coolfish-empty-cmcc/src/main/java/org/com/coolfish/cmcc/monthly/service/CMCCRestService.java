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
            logger.warn("处于空套餐的移动号码[{}]查询使用量失败。", message.getTel());
        } else {
            // 使用量 <=0
            double subResult = DecimalTools.sub(nowApiflow, String.valueOf(1024 * 10));
            if (DecimalTools.compareTo(subResult, 0d) > -1) {
                logger.info("处于空套餐的移动号码[{}]已使用量[{} kb],进行空套餐停机", message.getTel(), nowApiflow);
                disabledNumberController(operator, message);
            }
        }

    }

    // 停机操作
    public void disabledNumberController(CMCCOperator operator, MsisdnMessage message) {
        boolean cutSuccess = requestService.disabledNumber(operator, message.getTel());
        if (cutSuccess == true) {
            // 停机成功 ，更新到数据库,并记录停机日志
            databaseService.updateMsisdnSatus(message);
            logger.info("移动号码[{}]停机操作成功", message.getTel());
        } else {
            // 停机失败，传到cmcc-monthly85队列
            message.setRetry(1 + message.getRetry());
            logger.info("移动号码[{}]停机操作失败，传到cmcc-stoperror", message.getTel());
            rabbitTemplate.convertAndSend("cmcc-stoperror", JSON.toJSONString(message).toString());
        }
    }
}
