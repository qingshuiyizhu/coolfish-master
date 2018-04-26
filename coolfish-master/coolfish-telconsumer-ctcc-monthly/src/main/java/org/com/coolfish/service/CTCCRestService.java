package org.com.coolfish.service;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.com.coolfish.common.config.ParamConfig;
import org.com.coolfish.common.database.service.ComDBService;
import org.com.coolfish.common.message.MsisdnMessage;
import org.com.coolfish.common.model.CTCCOperator;
import org.com.coolfish.common.util.DecimalTools;
import org.com.coolfish.common.util.TimeUtils;
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
    @Autowired
    private RedisService redisService;

    @Autowired
    private ParamConfig param;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 电信停机
    public void handle(CTCCOperator operator, MsisdnMessage message) {
        // 查询电信当月使用量
        String nowApifow = requestService.queryTraffic(operator, message.getTel());
        double realApiflow = 0;
        if (nowApifow == null) {
            // 流程查询失败，传到队列ctcc-monthly85
            logger.info("电信号码[{}]本月使用量获取失败，传到队列ctcc-monthly85", message.getTel());
            rabbitTemplate.convertAndSend("ctcc-monthly85", JSON.toJSONString(message).toString());
        } else {
            realApiflow = DecimalTools.div(nowApifow, message.getPer(), 2);
            // 实际使用量
            message.setUseflow(String.valueOf(realApiflow));
            // 总流量-使用量<=0 判断是否停机
            double subResult = DecimalTools.sub(message.getSumflow(), Double.toString(realApiflow));
            // 使用量/总流量 判断是否重点监控
            double result85 = DecimalTools.div(Double.toString(realApiflow), message.getSumflow(), 2);

            java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            logger.info("电信号码[{}]的套餐总量[{} kb], 已使用量[{} kb], 使用率[{}%]", message.getTel(), message.getSumflow(),
                    nf.format(realApiflow), result85 * 100);
            message.setCardStatus("1");
            if (DecimalTools.compareTo(subResult, 0d) < 1) {
                // 获取号码状态
                String status = requestService.queryStauts(operator, message);
                if (StringUtils.isNotBlank(status)) {
                    if ("1".equals(status)) {
                        // 停机操作
                        logger.info("电信号码[{}]状态:[在用]进行停机操作", message.getTel());
                        disabledNumberController(operator, message);
                    } else {
                        logger.info("电信号码[{}]状态:[{}], 忽略停机操作", message.getTel(), status);
                    }
                    message.setCardStatus(status);
                }

                // 停机操作
                logger.info("电信号码停机操作：" + message.getTel());
                disabledNumberController(operator, message);
            } else if (DecimalTools.compareTo(result85, 0.85d) > -1) {
                // 传到电信月套餐使用率达85的队列
                // #更新使用量
                message.setUseflow(Double.toString(realApiflow));
                rabbitTemplate.convertAndSend("ctcc-monthly85", JSON.toJSONString(message).toString());
                logger.info("电信号码流量使用率达到85%，传到ctcc-monthly85：" + JSON.toJSONString(message).toString());
            } else {
                logger.info("电信号码流量使用量正常，不进行任何操作：" + message.getTel());
            }
            // 数据同步到Redis缓存上
            redisService.set(message.getTel(), JSON.toJSONString(message).toString());
            logger.info("电信号码信息更新到Redis缓存中,{}",  message.getTel());
            // 进行每日流量使用量记录写入数据库
            boolean isInsert = TimeUtils.isInDate(new Date(), param.getLogstartTime(), param.getLogendTime());
            if (isInsert) {
                databaseService.EveryDayLogWriter(message, realApiflow);
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
