package org.com.coolfish.service;

import java.util.Date;
import java.util.List;

import org.com.coolfish.entity.ChaOperator;
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
    private CHARestService chaRestService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    // 定时计划任务
    public void start() {
        ChaOperator operator = new ChaOperator("njplnxxjs", "ZGL@123456","487lhV125");
        String access_number = "1064973599126";
        chaRestService.queryTraffic(operator,access_number,null);
        
    }

   
}
