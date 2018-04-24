package org.com.coolfish.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.com.coolfish.entity.ChaOperator;
import org.com.coolfish.entity.KuyuAccount;
import org.com.coolfish.entity.KuyuOperator;
import org.com.coolfish.entity.MqJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

//电信月套餐队列
@Component
@RabbitListener(queues = "ctcc-silent")
public class MQReceiver {
    @Autowired
    private CTCCRestService ctccRestService;

    @Autowired
    private KuyuOperatorService kuyuOperatorService;

    @Autowired
    private KuyuAccountService kuyuAccountService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ChaOperator getoperator(Long id) {

        Map<Long, ChaOperator> map = null;
        if (map == null) {
            logger.info("无Zid，执行查询供应商账号信息");
            map = new HashMap<Long, ChaOperator>();
            List<KuyuOperator> operators = kuyuOperatorService.findAll1();
            ChaOperator operator = null;
            for (int i = 0; i < operators.size(); i++) {
                operator = JSON.parseObject(operators.get(i).getText(), ChaOperator.class);
                map.put(operators.get(i).getId(), operator);
            }
        }
        return map.get(id);

    }

    // 有Zid，执行查询供应商账号信息
    public ChaOperator getaccount(Long id) {
        Map<Long, ChaOperator> map = null;
        if (map == null) {
            logger.info("有Zid，执行查询供应商账号信息");
            map = new HashMap<Long, ChaOperator>();
            List<KuyuAccount> operators = kuyuAccountService.findAll1();
            ChaOperator operator = null;
            for (int i = 0; i < operators.size(); i++) {
                operator = JSON.parseObject(operators.get(i).getText(), ChaOperator.class);
                map.put(operators.get(i).getId(), operator);
            }
        }
        return map.get(id);
    }
    // 监听电信月套餐队列
    @RabbitHandler
    public void process(String json) {
        // 将String 转为MQJson对象
        MqJson mqJson = JSON.parseObject(json, MqJson.class);
        ChaOperator operator = null;
        if (0 < mqJson.getZid()) {
            // 有zid
            operator = getaccount(mqJson.getZid().longValue());
        } else {
            // 调用operator
            operator = getoperator(mqJson.getOperatorid().longValue());
        }
        if (null != operator) {
            // 调用流量监控服务
            ctccRestService.handSlient(operator, mqJson);
        } else {
            logger.error("没有找到供应商的账号信息,电信号码为：" + mqJson.getTel());
        }

    }

}
