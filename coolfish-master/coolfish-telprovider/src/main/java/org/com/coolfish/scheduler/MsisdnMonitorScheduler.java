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
public class MsisdnMonitorScheduler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KuyuCardService kuyuCardService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    private final static int CMCC = 1;

    private final static int CTCC = 2;

    private final static int MONTHLY = 1;

    private final static int TOTAL = 2;

    @Scheduled(cron = "0 0 0,8,13,20 * * ?")
    public void scheduledTask() {
        logger.info("#####执行定时计划任务：查询正常状态下所有卡片及套餐信息#####");
        start();
    }

    /**
     * 1. 获取已订购月套餐的所有号码 2.查询总使用量 3.判断是不是电信还是移动的
     * 
     * 4.封装json数据 5.传到对队列
     * 
     * 
     */
    public void start() {
        // 组合查询出卡号码的供应商id，套餐总流量等信息
        List<KuyuCard> list = kuyuCardService.findCardMessage();
        logger.info("已订购月套餐的所有号码数量：" + list.size());
        for (KuyuCard card : list) {
            // 是否月套餐
            if (MONTHLY == card.getType()) {
                MsisdnMessage message = new MsisdnMessage(card.getTel(), card.getOperatorid(), card.getZid(),
                        null, null, String.valueOf(card.getSumflow()), String.valueOf(card.getUseflow()),
                        String.valueOf(card.getPer()), 0, 0, card.getId(), null,
                        String.valueOf(card.getCard_status()));

                String json = JSON.toJSONString(message).toString();
                // 移动月套餐
                if (CMCC == card.getOperator_type()) {
                    rabbitTemplate.convertAndSend("cmcc-monthly", json);
                    logger.info("加入移动月套餐队列(cmcc-monthly),封装的message为：" + json);
                    // 电信月套餐
                } else if (CTCC == card.getOperator_type()) {
                    rabbitTemplate.convertAndSend("ctcc-monthly", json);
                    logger.info("加入电信月套餐队列(ctcc-monthly),封装的message为：" + json);
                }
                // 累积套餐
            } else if (TOTAL == card.getType()) {
                // logger.info("累积套餐" + card.toString());
            }
        }

    }

}
