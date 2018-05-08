package org.com.coolfish.scheduler;

import java.util.List;

import org.com.coolfish.common.database.entity.KuyuCard;
import org.com.coolfish.common.database.service.KuyuCardService;
import org.com.coolfish.common.message.MsisdnMessage;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class SilentScheduler {

    @Autowired
    private KuyuCardService kuyuCardService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin rabbitAdmin;

   
    // 累计套餐沉默期监控
   // @Scheduled(cron = "0 0/5 * * * ?")
    public void silentScheduler() {
        Integer silent = Integer.parseInt(
                rabbitAdmin.getQueueProperties("total-silent").get("QUEUE_MESSAGE_COUNT").toString());
     if (silent < 100) {
            startSlient();
        }
    }

    public void startSlient() {
        log.info("#####执行累计套餐沉默期号码查询#####");
        // 组合查询出卡号码的供应商id，套餐总流量等信息
        List<KuyuCard> list = kuyuCardService.findSilentCardMessage();
        int number = list.size();
        log.info("进行累计套餐沉默期处理所有号码数量：{}", number);
        for (KuyuCard card : list) {
            log.info("剩余未处理号码个数：[{}],正在处理物联网卡ID[{}]数据:{}", number--, card.getId(), card.toString());
            MsisdnMessage message = new MsisdnMessage();
            message.setCardid(card.getId());
            message.setIphone(card.getTel());
            // message.setIccid(card.get);
            message.setOperatorType(card.getOperator_type());
            String json = JSON.toJSONString(message).toString();
            rabbitTemplate.convertAndSend("total-silent", json);
            log.info("物联网卡ID[{}]加入累计套餐沉默期处理队列(total-silent),封装的message为：{}",card.getId(),json);

        }

    }
}
