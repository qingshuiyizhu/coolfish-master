package org.com.coolfish.cmcc.monthly.service;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.com.coolfish.common.config.ParamConfig;
import org.com.coolfish.common.database.service.ComDBService;
import org.com.coolfish.common.message.MsisdnMessage;
import org.com.coolfish.common.model.CMCCOperator;
import org.com.coolfish.common.util.DecimalTools;
import org.com.coolfish.common.util.TimeUtils;
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
    private ComDBService databaseService;

    @Autowired
    private CMCCRequestService requestService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private ParamConfig param;

    @Autowired
    private RedisService redisService;

    /**
     * 移动流量监控方法
     * 
     * @param operator
     * @param msisdnMessage 1.通过API查询当月的月套餐流量使用量 2.
     */
    public void handle(CMCCOperator operator, MsisdnMessage message) {
        // 1. 查询号码使用量
        String nowApiflow = requestService.queryTraffic(operator, message);
        double realApiflow = 0;
        // 2.1使用量为null 查询失败
        if (nowApiflow == null) {
            logger.warn("移动号码[{}]查询使用量失败，传回队列cmcc-monthly85", message.getTel());
            // 流程查询失败，传回队列cmcc-monthly85
            rabbitTemplate.convertAndSend("cmcc-monthly85", JSON.toJSONString(message).toString());
        } else {
            // 2.2查询成功 除于流量比例 得到实际使用量
            realApiflow = DecimalTools.div(nowApiflow, message.getPer(), 2);

            // 实际使用量
            message.setUseflow(String.valueOf(realApiflow));
            // 总流量-使用量 <=0
            double subResult = DecimalTools.sub(message.getSumflow(), Double.toString(realApiflow));
            // 使用量/总流量
            double result85 = DecimalTools.div(Double.toString(realApiflow), message.getSumflow(), 2);

            java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            logger.info("移动号码[{}]的套餐总量[{} kb], 已使用量[{} kb], 使用率[{}%]", message.getTel(), message.getSumflow(),
                    nf.format(realApiflow), result85 * 100);
            message.setCardStatus("1");
            if (DecimalTools.compareTo(subResult, 0d) < 1) {
                // 获取号码状态
                String status = requestService.queryStauts(operator, message);
                if (StringUtils.isNotBlank(status)) {
                    if ("1".equals(status)) {
                        // 停机操作
                        logger.info("移动号码[{}]进行停机操作", message.getTel());
                        disabledNumberController(operator, message);
                    } else {
                        String statusDesc = ("2".equals(status)) ? "半停" : "全停";
                        logger.info("移动号码[{}]状态:[{}], 忽略停机操作", message.getTel(), statusDesc);
                    }
                    message.setCardStatus("已停机");
                }
            } else if (DecimalTools.compareTo(result85, 0.85d) > -1) {
                // 传到电信月套餐使用率达85的队列
                // #更新使用量
                message.setUseflow(Double.toString(realApiflow));
                rabbitTemplate.convertAndSend("cmcc-monthly85", JSON.toJSONString(message).toString());
                logger.info("移动号码[{}]流量使用率达到85%，传到cmcc-monthly85队列：{}", message.getTel(),
                        JSON.toJSONString(message).toString());
            }
            
            // 数据同步到Redis缓存上
            redisService.set(message.getTel(), JSON.toJSONString(message).toString());
            logger.info("移动号码信息更新到Redis缓存中,{}",  message.getTel());

            // 进行每日流量使用量记录写入数据库 ,判断是否在记录时间内
            boolean isInsert = TimeUtils.isInDate(new Date(), param.getLogstartTime(), param.getLogendTime());
            if (isInsert) {
                databaseService.EveryDayLogWriter(message, realApiflow);
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
