package org.com.coolfish.service;

import org.com.coolfish.common.database.service.ComDBService;
import org.com.coolfish.common.message.MsisdnMessage;
import org.com.coolfish.common.model.CTCCOperator;
import org.com.coolfish.common.util.DecimalTools;
import org.com.coolfish.common.webinterface.service.CTCCResquestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

/***
 * 
 * @author LINGHUI
 * @desc 中国电信的http get 请求API
 *
 */
@Service
public class CTCCRestService {

    @Autowired
    private ComDBService databaseService;

    @Autowired
    private CTCCResquestService requestService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 电信停机
    public void handle(CTCCOperator operator, MsisdnMessage message) {
        // 查询电信当月使用量
        String nowApiflow = requestService.queryTraffic(operator, message.getTel());
        if (nowApiflow == null) {
            // 流程查询失败，传到队列ctcc-monthly85
            logger.info("处于空套餐的电信号码[{}]查询使用量失败。", message.getTel());
        } else {
            double subResult = DecimalTools.sub(nowApiflow, String.valueOf(1024 * 10));
            if (DecimalTools.compareTo(subResult, 0d) > -1) {
                logger.info("处于空套餐的移动号码[{}]已使用量[{} kb],进行空套餐停机操作", message.getTel(), nowApiflow);
                disabledNumberController(operator, message);
            }
        }

    }

    // 电信停机处理方法
    public void disabledNumberController(CTCCOperator operator, MsisdnMessage message) {
        boolean cutSuccess = requestService.disabledNumber(operator, message.getTel());
        if (cutSuccess == true) {
            // 停机成功 ，更新到数据库,并记录停机日志
            databaseService.updateMsisdnSatus(message);
            logger.info("电信号码[{}]停机操作成功", message.getTel());
        } else {
            logger.info("电信号码[{}]停机操作失败，传到ctcc-stoperror队列", message.getTel());
            rabbitTemplate.convertAndSend("ctcc-stoperror", JSON.toJSONString(message).toString());
        }
    }
}
