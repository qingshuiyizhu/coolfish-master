package org.com.coolfish.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.com.coolfish.entity.KuyuAccount;
import org.com.coolfish.entity.KuyuOperator;
import org.com.coolfish.entity.MqJson;
import org.com.coolfish.entity.OperatorText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

//移动月套餐队列
@Component
@RabbitListener(queues = "cmcc-monthly")
public class MQReceiver {
    @Autowired
    private CMCCRestService cmccRestService;

    @Autowired
    private KuyuOperatorService kuyuOperatorService;

    @Autowired
    private KuyuAccountService kuyuAccountService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public OperatorText getoperator(Long id) {

        Map<Long, OperatorText> map = null;
        if (map == null) {
            logger.info("无Zid，执行查询供应商账号信息");
            map = new HashMap<Long, OperatorText>();
            List<KuyuOperator> operators = kuyuOperatorService.findByOpid(1);
            OperatorText operator = null;
            for (int i = 0; i < operators.size(); i++) {
                operator = JSON.parseObject(operators.get(i).getText(), OperatorText.class);
                map.put(operators.get(i).getId(), operator);
            }
        }
        return map.get(id);

    }

    public OperatorText getaccount(Long id) {
        Map<Long, OperatorText> map = null;
        if (map == null) {
            logger.info("有Zid，执行查询供应商账号信息");
            map = new HashMap<Long, OperatorText>();
            List<KuyuAccount> operators = kuyuAccountService.findByOpid(1);
            OperatorText operator = null;
            for (int i = 0; i < operators.size(); i++) {
                operator = JSON.parseObject(operators.get(i).getText(), OperatorText.class);
                map.put(operators.get(i).getId(), operator);
            }
        }
        return map.get(id);
    }

    @RabbitHandler
    public void process(String json) {
        // 将String 转为MQJson对象
        MqJson mqJson = JSON.parseObject(json, MqJson.class);
        OperatorText operator = null;
        if (0 < mqJson.getZid()) {
            // 有zid
            operator = getoperator(mqJson.getZid().longValue());
        } else {
            // 调用operator
            operator = getoperator(mqJson.getOperatorid().longValue());
        }

        if (null != operator) {
            // 调用流量监控服务
            cmccRestService.LiuLiangControl(operator, mqJson);
        } else {
            logger.error("没有找到供应商的账号信息,号码为：" + mqJson.getTel());
        }

    }

}
