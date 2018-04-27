package org.com.coolfish.scheduler;

import java.util.List;

import org.com.coolfish.common.database.entity.KuyuCard;
import org.com.coolfish.common.database.service.KuyuCardService;
import org.com.coolfish.common.message.MsisdnMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

@Service
public class MsisdnSilentScheduler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KuyuCardService kuyuCardService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    private final static int CMCC = 1;

    private final static int CTCC = 2;

    @Scheduled(cron = "0 0 9,15,22 * * ?")
    public void startSlient() {
        logger.info("#####执行定时计划任务：执行沉默期号码查询#####");
        // 组合查询出卡号码的供应商id，套餐总流量等信息
        List<KuyuCard> list = kuyuCardService.findSilentCardMessage();
        logger.info("进行沉默期处理所有号码数量：" + list.size());
        for (KuyuCard card : list) {
            MsisdnMessage message = new MsisdnMessage(card.getTel(), card.getOperatorid(), card.getZid(),
                    null, null, String.valueOf(card.getSumflow()), String.valueOf(card.getUseflow()),
                    String.valueOf(card.getPer()), 0, 0, card.getId(), null,
                    String.valueOf(card.getCard_status()));
            String json = JSON.toJSONString(message).toString();
            // 移动号码沉默期处理队列
            if (CMCC == card.getOperator_type()) {
                rabbitTemplate.convertAndSend("cmcc-silent", json);
                logger.info("加入移动号码沉默期处理队列(cmcc-silent),封装的message为：" + json);
                // 电信号码沉默期处理队列
            } else if (CTCC == card.getOperator_type()) {
                rabbitTemplate.convertAndSend("ctcc-silent", json);
                logger.info("加入电信号码沉默期处理队列(ctcc-silent),封装的message为：" + json);
            }

        }
    }

}
