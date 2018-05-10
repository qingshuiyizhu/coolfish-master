package org.com.coolfish.scheduler;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.com.coolfish.common.database.entity.KuyuCard;
import org.com.coolfish.common.database.service.KuyuCardService;
import org.com.coolfish.common.message.MsisdnMessage;
import org.com.coolfish.common.util.DecimalTools;
import org.com.coolfish.service.RedisService;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MonthlyScheduler {

    @Autowired
    private KuyuCardService kuyuCardService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin rabbitAdmin;

    @Autowired
    private RedisService redisService;

    // 月套餐监控
    @Scheduled(cron = "0 0/5 * * * ?")
    public void querymonthlyScheduler() {
        Integer query = Integer.parseInt(
                rabbitAdmin.getQueueProperties("query-monthly").get("QUEUE_MESSAGE_COUNT").toString());
        Integer fastquery = Integer.parseInt(
                rabbitAdmin.getQueueProperties("fastquery-monthly").get("QUEUE_MESSAGE_COUNT").toString());
        if (query < 5) {
            startMonthly();
            return;
        } else if (fastquery < 5) {
            startFastQueryMonthly();
        }
    }

    private void startFastQueryMonthly() {
        log.info("#####月套餐快速通道流量监控-->查询正常状态下所有月套餐卡片及信息#####");
        List<KuyuCard> list = kuyuCardService.findMonthlyCardMessage();
        int number = list.size();
        log.info("已订购月套餐的所有号码数量：{}", number);
        for (KuyuCard card : list) {
            log.info("剩余未处理号码个数：[{}],正在处理物联网卡ID[{}]数据:{}", number--, card.getId(), card.toString());
            // 使用量
            String userflow = null;
            MsisdnMessage msisdnMessage = null;
            try {
                String redisMessage = redisService.get(String.valueOf(card.getId()));
                msisdnMessage = JSON.parseObject(redisMessage, MsisdnMessage.class);
                userflow = msisdnMessage.getUseflow();
                log.info("Redis缓存获取信息：{}", redisMessage);
            } catch (Exception e) {
                noCache(card);
                continue;
            }

            // 缓存中有数据
            double rate = 0.0D;
            java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            if (StringUtils.isNotBlank(userflow)) {
                // 如果总流量发生变化，更新redis数据
                if (DecimalTools.compareTo(card.getSumflow(), msisdnMessage.getSumflow()) != 0) {
                    msisdnMessage.setUseflow(String.valueOf(card.getSumflow()));
                    redisService.set(String.valueOf(card.getId()),
                            JSON.toJSONString(msisdnMessage).toString());
                }
                rate = DecimalTools.div(userflow, msisdnMessage.getSumflow(), 2);
                if (DecimalTools.compareTo(rate, 0.90D) > -1) {
                    rabbitTemplate.convertAndSend("fastquery-monthly",
                            JSON.toJSONString(msisdnMessage).toString());
                    log.info(
                            "物联网卡ID[{}]套餐总量[{} kb], 已使用量[{} kb], 使用率[{}%],流量预警,传到fastquery-monthly队列进行流量状态查询{}",
                            card.getId(), String.valueOf(card.getSumflow()), userflow, rate * 100,
                            JSON.toJSONString(msisdnMessage).toString());
                }
            }
        }

    }

    public void startMonthly() {
        log.info("#####月套餐流量监控-->查询正常状态下所有月套餐卡片及信息#####");
        List<KuyuCard> list = kuyuCardService.findMonthlyCardMessage();
        int number = list.size();
        log.info("已订购月套餐的所有号码数量：{}", number);
        for (KuyuCard card : list) {
            log.info("剩余未处理号码个数：[{}],正在处理物联网卡ID[{}]数据:{}", number--, card.getId(), card.toString());
            // 使用量
            String userflow = null;
            MsisdnMessage msisdnMessage = null;
            try {
                String redisMessage = redisService.get(String.valueOf(card.getId()));
                msisdnMessage = JSON.parseObject(redisMessage, MsisdnMessage.class);
                userflow = msisdnMessage.getUseflow();
                log.info("Redis缓存获取信息：{}", redisMessage);
            } catch (Exception e) {
                noCache(card);
                continue;
            }

            // 缓存中有数据
            double rate = 0.0D;
            java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            if (StringUtils.isNotBlank(userflow)) {
                // 如果总流量发生变化，更新redis数据
                if (DecimalTools.compareTo(card.getSumflow(), msisdnMessage.getSumflow()) != 0) {
                    msisdnMessage.setUseflow(String.valueOf(card.getSumflow()));
                    redisService.set(String.valueOf(card.getId()),
                            JSON.toJSONString(msisdnMessage).toString());
                }
                rate = DecimalTools.div(userflow, msisdnMessage.getSumflow(), 2);
                if (DecimalTools.compareTo(rate, 0.90D) > -1) {
                    rabbitTemplate.convertAndSend("fastquery-monthly",
                            JSON.toJSONString(msisdnMessage).toString());
                    log.info(
                            "物联网卡ID[{}]套餐总量[{} kb], 已使用量[{} kb], 使用率[{}%],流量预警,传到fastquery-monthly队列进行流量状态查询{}",
                            card.getId(), String.valueOf(card.getSumflow()), userflow, rate * 100,
                            JSON.toJSONString(msisdnMessage).toString());
                } else {
                    rabbitTemplate.convertAndSend("query-monthly",
                            JSON.toJSONString(msisdnMessage).toString());
                    log.info(
                            "物联网卡ID[{}]套餐总量[{} kb], 已使用量[{} kb], 使用率[{}%],流量使用正常,传到query-monthly队列进行流量状态查询{}",
                            card.getId(), String.valueOf(card.getSumflow()), userflow, rate * 100,
                            JSON.toJSONString(msisdnMessage).toString());
                }
            }
        }

    }

    // Redis缓存中没有数据
    private void noCache(KuyuCard card) {
        MsisdnMessage msisdnMessage = new MsisdnMessage();
        msisdnMessage.setCardid(card.getId());
        msisdnMessage.setStartusCode(String.valueOf(card.getCard_status()));
        msisdnMessage.setIphone(card.getTel());
        msisdnMessage.setIccid(card.getIccid());
        msisdnMessage.setPer(String.valueOf(card.getPer()));
        msisdnMessage.setOperatorid(card.getOperatorid());
        msisdnMessage.setCardStatus("在用");
        msisdnMessage.setZid(card.getZid());
        msisdnMessage.setUseflow("0.01");
        msisdnMessage.setSumflow(String.valueOf(card.getSumflow()));
        msisdnMessage.setOperatorType(card.getOperator_type());
        double rate = DecimalTools.div(msisdnMessage.getUseflow(), msisdnMessage.getSumflow(), 2);
        rabbitTemplate.convertAndSend("fastquery-monthly", JSON.toJSONString(msisdnMessage).toString());
        log.info("物联网卡ID[{}]套餐总量[{} kb], 已使用量[{} kb], 使用率[{}%],缓存无数据,传到fastquery-monthly队列进行流量状态查询{}",
                card.getId(), String.valueOf(card.getSumflow()), String.valueOf(card.getUseflow()),
                rate * 100, JSON.toJSONString(msisdnMessage).toString());
    }
}
