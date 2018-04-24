package org.com.coolfish.service;

import java.util.Date;
import java.util.List;

import org.com.coolfish.entity.KuyuCard;
import org.com.coolfish.entity.MqJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MainService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CHARestService restTemplateService;

    @Autowired
    private KuyuCardService kuyuCardService;

    @Autowired
    private KuyuAddPackageService addPackageService;

    @Autowired
    private CMCCRestService cmccRestService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    // 定时计划任务
    public void start() {

      double s = addPackageService.SumFlowTal("123456789").doubleValue(); 
        System.out.println(s);
    }

    public void start4() {
        List<KuyuCard> list = kuyuCardService.findInA();
        MqJson json = null;
        KuyuCard card = null;
        for (int i = 0; i < list.size(); i++) {
            card = list.get(i);
           
            if (1 == card.getType()) {
                // 月套餐
                // 根据tel查询表kuyu_add_package得到该接入号的总流量
                // select sum(e.sumflow) from kuyu_add_package e where e.tel =:tel and e.status=2
                // and e.type in(1,4);
                double sumFlow = addPackageService.SumFlow(card.getTel()).doubleValue();
                json = new MqJson(card.getTel(), card.getOperatorid(), card.getZid(), sumFlow,
                        card.getUseflow().doubleValue(), card.getPer().doubleValue());
                if (1 == card.getOperator_type()) {
                    rabbitTemplate.convertAndSend("cmcc-monthly", json);
                    logger.info("加入移动月套餐队列：" + json);
                } else if (2 == card.getOperator_type()) {
                    rabbitTemplate.convertAndSend("cha-monthly", json);
                    logger.info("加入电信月套餐队列：" + json);
                } else if (3 == card.getOperator_type()) {
                    rabbitTemplate.convertAndSend("unicom-monthly", json);
                    logger.info("加入联通月套餐队列：" + json);
                } else {
                    logger.error("运营商未知号码：" + json);
                }
            } else if (2 == card.getType()) {
                // 累计套餐
              double sumFlow = addPackageService.SumFlowTal(card.getTel()).doubleValue();
                json = new MqJson(card.getTel(), card.getOperatorid(), card.getZid(), sumFlow,
                        card.getUseflow().doubleValue(), card.getPer().doubleValue());
                rabbitTemplate.convertAndSend("cmcc-total", json);
                logger.info("加入累计套餐队列：" + json);
            } else {
                logger.error("套餐类型不匹配：" + card.toString());
            }

        }
    }
    public void start3() {

        String context = "123456789" + new Date();
        System.out.println("Sender : " + context);
        rabbitTemplate.convertAndSend("cmcc-monthly", context);
        System.out.println(rabbitTemplate.toString() + "--------------111");
    }

    public void start1() {

        List<KuyuCard> list = kuyuCardService.findInA();
        for (int i = 0; i < list.size(); i++) {
            // restTemplateService.queryCardStatus("1064915327521", "20180301", i);
            System.out.println(list.get(i).getTel());

        }
    }
}
