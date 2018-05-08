package org.com.coolfish.scheduler;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.com.coolfish.common.database.entity.KuyuCard;
import org.com.coolfish.common.database.service.KuyuCardService;
import org.com.coolfish.common.message.MsisdnMessage;
import org.com.coolfish.common.util.DecimalTools;
import org.com.coolfish.service.ActionService;
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
public class MsisdnMonthlyScheduler {

    @Autowired
    private KuyuCardService kuyuCardService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin rabbitAdmin;
    @Autowired
    private ActionService action;
    @Autowired
    private RedisService redisService;

    private static final int CMCC = 1;

    private static final int CTCC = 2;

    // 月套餐监控
    @Scheduled(cron = "0 0/5 * * * ?")
    public void monthlyScheduler() {
        Integer ctcc = Integer.parseInt(
                rabbitAdmin.getQueueProperties("fastquery-ctcc").get("QUEUE_MESSAGE_COUNT").toString());
        Integer cmcc = Integer.parseInt(
                rabbitAdmin.getQueueProperties("fastquery-cmcc").get("QUEUE_MESSAGE_COUNT").toString());
        if (ctcc < 100 || cmcc < 100) {
            startMonthly();
        }
    }

    public void startMonthly() {
        log.info("#####月套餐监控-->查询正常状态下所有月套餐卡片及信息#####");
        List<KuyuCard> list = kuyuCardService.findCardMessage();
        int number = list.size();
        log.info("已订购月套餐的所有号码数量：" + number);

        for (KuyuCard card : list) {
            log.info("剩余未处理号码个数：[{}],正在处理物联网卡ID[{}]数据:{}",number--, card.toString());
             // 使用量
            String userflow = null;
            MsisdnMessage msisdnMessage = null;
            try {
                String redisMessage = redisService.get(String.valueOf(card.getId()));
                msisdnMessage = JSON.parseObject(redisMessage, MsisdnMessage.class);
                userflow = msisdnMessage.getUseflow();
                log.info("Redis缓存获取信息：{}", redisMessage);
            } catch (Exception e) {
                setCache(card);
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
                if (DecimalTools.compareTo(rate, 0.99D) > -1) {
                    // 流量用完进行停机
                    if ("1".equals(msisdnMessage.getStartusCode())) {
                        // 状态，正常 停机
                        action.stop(card, userflow, rate,"月套餐流量用完");
                    } else {
                        // 更新数据库卡状态 ,删除redis上数据
                        kuyuCardService.editStatus(4, card.getId());
                        redisService.delete(String.valueOf(card.getId()));
                    }
                } else if (DecimalTools.compareTo(rate, 0.90D) > -1) {
                    if (CMCC == card.getOperator_type()) {
                        rabbitTemplate.convertAndSend("fastquery-cmcc",
                                JSON.toJSONString(msisdnMessage).toString());
                        log.info(
                                "移动物联网卡ID[{}]套餐总量[{} kb], 已使用量[{} kb], 使用率[{}%],流量预警,传到fastquery-cmcc队列进行流量状态查询{}",
                                card.getId(), String.valueOf(card.getSumflow()), userflow, rate * 100,
                                JSON.toJSONString(msisdnMessage).toString());

                    } else if (CTCC == card.getOperator_type()) {
                        rabbitTemplate.convertAndSend("fastquery-ctcc",
                                JSON.toJSONString(msisdnMessage).toString());
                        log.info(
                                "电信物联网卡ID[{}]套餐总量[{} kb], 已使用量[{} kb], 使用率[{}%],流量预警,传到fastquery-ctcc队列进行流量状态查询{}",
                                card.getId(), String.valueOf(card.getSumflow()), userflow, rate * 100,
                                JSON.toJSONString(msisdnMessage).toString());
                    }
                } else {
                    if (CMCC == card.getOperator_type()) {
                        rabbitTemplate.convertAndSend("query-cmcc",
                                JSON.toJSONString(msisdnMessage).toString());
                        log.info(
                                "移动物联网卡ID[{}]套餐总量[{} kb], 已使用量[{} kb], 使用率[{}%],流量使用正常,传到query-cmcc队列进行流量状态查询{}",
                                card.getId(), String.valueOf(card.getSumflow()), userflow, rate * 100,
                                JSON.toJSONString(msisdnMessage).toString());
                    } else if (CTCC == card.getOperator_type()) {
                        rabbitTemplate.convertAndSend("query-ctcc",
                                JSON.toJSONString(msisdnMessage).toString());
                        log.info(
                                "电信物联网卡ID[{}]套餐总量[{} kb], 已使用量[{} kb], 使用率[{}%],流量使用正常,传到query-ctcc队列进行流量状态查询{}",
                                card.getId(), String.valueOf(card.getSumflow()), userflow, rate * 100,
                                JSON.toJSONString(msisdnMessage).toString());
                    }
                }
            }

        }

    }

    // Redis缓存中没有数据
    private void setCache(KuyuCard card) {
        MsisdnMessage msisdnMessage = new MsisdnMessage();
        msisdnMessage.setCardid(card.getId());
        msisdnMessage.setStartusCode(String.valueOf(card.getCard_status()));
        msisdnMessage.setIphone(card.getTel());
        msisdnMessage.setPer(String.valueOf(card.getPer()));
        msisdnMessage.setOperatorid(card.getOperatorid());
        msisdnMessage.setCardStatus("在用");
        msisdnMessage.setZid(card.getZid());
        msisdnMessage.setSumflow(String.valueOf(card.getSumflow()));
        msisdnMessage.setUseflow(String.valueOf(card.getUseflow()));
        msisdnMessage.setSumflow(String.valueOf(card.getSumflow()));

       redisService.set(String.valueOf(card.getId()), JSON.toJSONString(msisdnMessage).toString());

        double rate = DecimalTools.div(msisdnMessage.getUseflow(), msisdnMessage.getSumflow(), 2);

        if (CMCC == card.getOperator_type()) {
            rabbitTemplate.convertAndSend("fastquery-cmcc", JSON.toJSONString(msisdnMessage).toString());
            log.info("移动物联网卡ID[{}]套餐总量[{} kb], 已使用量[{} kb], 使用率[{}%],缓存无数据,传到fastquery-cmcc队列进行流量状态查询{}",
                    card.getId(), String.valueOf(card.getSumflow()), String.valueOf(card.getUseflow()),
                    rate * 100, JSON.toJSONString(msisdnMessage).toString());

        } else if (CTCC == card.getOperator_type()) {
            rabbitTemplate.convertAndSend("fastquery-ctcc", JSON.toJSONString(msisdnMessage).toString());
            log.info("电信物联网卡ID[{}]套餐总量[{} kb], 已使用量[{} kb], 使用率[{}%],缓存无数据,传到fastquery-ctcc队列进行流量状态查询{}",
                    card.getId(), String.valueOf(card.getSumflow()), String.valueOf(card.getUseflow()),
                    rate * 100, JSON.toJSONString(msisdnMessage).toString());
        }
    }

  

  /* public void monthlyTest() {
        // 流量使用超过99%，进行停机
        DisabledBean disabledBean = new DisabledBean();
        disabledBean.setActionReason("月套餐流量用完");
        disabledBean.setCardId(532868   );
        disabledBean.setIphone("1064952404809");
        disabledBean.setOperatorid(189);
        disabledBean.setZid(0);
        disabledBean.setOperatorType(2);
        disabledBean.setOprtype("1");//
        disabledBean.setReason("2");//
        disabledBean.setOperatorName("流量监控");
        disabledBean.setSource("流量监控");
            rabbitTemplate.convertAndSend("switch-ctcc", JSON.toJSONString(disabledBean).toString());
          
    } */

}
