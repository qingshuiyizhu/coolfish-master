package org.com.coolfish.scheduler;

import java.util.List;

import org.com.coolfish.common.database.entity.KuyuCard;
import org.com.coolfish.common.database.service.KuyuCardService;
import org.com.coolfish.common.message.MsisdnMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class EmptyScheduler {

    @Autowired
    private KuyuCardService kuyuCardService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin rabbitAdmin;
 
    public void monthlyScheduler() {
        Integer empty = Integer.parseInt(
                rabbitAdmin.getQueueProperties("empty-monthly").get("QUEUE_MESSAGE_COUNT").toString());
        if (empty < 100) {
            startEmpty();
        }
    }

    public void startEmpty() {
        log.info("#####执行计划任务：执行移动池卡空套餐号码查询#####");
        /*
         * card_type=2 and type = 4 移动池卡空套餐使用流量超过10M的停机
         * 
         */
        // 组合查询出卡号码的供应商id，套餐总流量等信息
        Integer[] operatorIds = new Integer[] { 118, 137, 142, 143, 159, 160, 167, 170, 171, 173, 179, 180,
                181, 182, 183, 185, 191, 192, 193, 194, 202, 203, 204, 209, 210, 211, 213 };
        List<KuyuCard> list = kuyuCardService.findEmptyCardMessage(operatorIds);
        log.info("进行空套餐处理所有号码数量：" + list.size());
        for (KuyuCard card : list) {
            MsisdnMessage message = new MsisdnMessage();
            message.setCardid(card.getId());
            message.setIphone(card.getTel());
            message.setOperatorid(card.getOperatorid());
            message.setOperatorType(card.getOperator_type());
            String json = JSON.toJSONString(message).toString();
            rabbitTemplate.convertAndSend("empty-monthly", json);
            log.info("物联网卡ID[{}]加入月套餐空套餐处理队列(empty-monthly),封装的message为：{}", card.getId(), json);
        }
    }

}
