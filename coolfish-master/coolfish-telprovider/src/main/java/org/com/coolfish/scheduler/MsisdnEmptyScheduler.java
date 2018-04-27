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
public class MsisdnEmptyScheduler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KuyuCardService kuyuCardService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    private final static int CMCC = 1;

    private final static int CTCC = 2;

    @Scheduled(cron = "0 0 5 * * ?")
    public void startEmpty() {
        logger.info("#####执行定时计划任务：执行空套餐号码查询#####");
        // 组合查询出卡号码的供应商id，套餐总流量等信息
        Integer[] operatorIds = new Integer[] { 118, 137, 142, 143, 159, 160, 167, 170, 171, 173, 179, 180,
                181, 182, 183, 185, 191, 192, 193, 194, 202, 203, 204, 209, 210, 211, 213 };
        List<KuyuCard> list = kuyuCardService.findEmptyCardMessage(operatorIds);
        logger.info("进行空套餐处理所有号码数量：" + list.size());
        for (KuyuCard card : list) {
            MsisdnMessage message = new MsisdnMessage(card.getTel(), card.getOperatorid(), card.getZid(),
                    null, null, String.valueOf(card.getSumflow()), String.valueOf(card.getUseflow()),
                    String.valueOf(card.getPer()), 0, 0, card.getId(), null,
                    String.valueOf(card.getCard_status()));
            String json = JSON.toJSONString(message).toString();
            // 移动号码空套餐处理队列
            if (CMCC == card.getOperator_type()) {
                rabbitTemplate.convertAndSend("cmcc-empty", json);
                logger.info("加入移动号码空套餐处理队列(cmcc-empty),封装的message为：" + json);
                // 电信号码空套餐处理队列
            } else if (CTCC == card.getOperator_type()) {
                rabbitTemplate.convertAndSend("ctcc-empty", json);
                logger.info("加入电信号码空套餐处理队列(ctcc-empty),封装的message为：" + json);
            }

        }
    }

}
