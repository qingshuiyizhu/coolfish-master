package org.com.coolfish.lister.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.com.coolfish.common.database.service.ComDBService;
import org.com.coolfish.common.message.MsisdnMessage;
import org.com.coolfish.common.model.UtilBean;
import org.com.coolfish.common.util.DecimalTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author LINGHUI
 * @desc 中国移动http 请求API
 */
@Service
@Slf4j
public class TotalSilentService {

    @Autowired
    private ComDBService databaseService;

    @Autowired
    private RestTemplate restTemplate;

    private final static int CMCC = 1;

    private final static int CTCC = 2;

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void handle(String message) {
        // 将String 转为MsisdnMessage对象
        log.info("累积套餐沉默期处理队列(total-silent)获取数据[{}]", message);
        MsisdnMessage msisdnMessage = JSON.parseObject(message, MsisdnMessage.class);
        long startTime = System.currentTimeMillis();
        log.info("物联网卡ID[{}]开始执行累积套餐沉默期处理, 开始执行时间:{}, 时间戳:{}", msisdnMessage.getCardid(),
                sdf.format(new Date(startTime)), startTime);
        handleMessage(msisdnMessage);
        long endTime = System.currentTimeMillis();
        log.info("物联网卡ID[{}]结束执行累积套餐沉默期处理, 结束执行时间:{}, 时间戳:{}, 耗时:{}ms", msisdnMessage.getCardid(),
                sdf.format(new Date(endTime)), endTime, (endTime - startTime));
    }

    @Async
    public void handleMessage(MsisdnMessage msisdnMessage) {
        if (CMCC == msisdnMessage.getOperatorType()) {
            log.info("物联网卡ID[{}]移动累积套餐沉默期处理", msisdnMessage.getCardid());
            handleCMCCQuery(msisdnMessage);

        } else if (CTCC == msisdnMessage.getOperatorType())
            log.info("物联网卡ID[{}]电信累积套餐沉默期处理", msisdnMessage.getCardid());
        handleCTCCQuery(msisdnMessage);
    }

    public void handleCMCCQuery(MsisdnMessage msisdnMessage) {
        UtilBean requestBean = new UtilBean();
        requestBean.setCardId(msisdnMessage.getCardid());
        requestBean.setIccid(msisdnMessage.getIccid());
        requestBean.setIphone(msisdnMessage.getIphone());
        requestBean.setOperatorid(msisdnMessage.getOperatorid());
        requestBean.setOperatorType(msisdnMessage.getOperatorType());
        requestBean.setZid(msisdnMessage.getZid());
        try {
            requestBean = restTemplate.postForEntity("http://114.55.132.207:8761/cmcc/queryTraffic",
                    requestBean, UtilBean.class).getBody();
        } catch (Exception e) {
            log.error("物联网卡ID[{}]查询总流量出错,{}", msisdnMessage.getCardid(), e.getMessage());
        }

        if (StringUtils.isNotBlank(requestBean.getAnalyze())) {
            // 实际使用量
            double subResult = DecimalTools.sub(requestBean.getAnalyze(), "1.00");
            // 开始使用流量，更新数据库套餐表的开始时间和结束时间
            if (DecimalTools.compareTo(subResult, 0d) > -1) {
                log.info("物联网卡ID[{}]移动已使用量[{} kb],进行套餐包激活", requestBean.getCardId(),
                        requestBean.getAnalyze());
                databaseService.flashSlientStatus(requestBean.getCardId());
            }

        }

    }

    public void handleCTCCQuery(MsisdnMessage msisdnMessage) {
        UtilBean requestBean = new UtilBean();
        requestBean.setCardId(msisdnMessage.getCardid());
        requestBean.setIccid(msisdnMessage.getIccid());
        requestBean.setIphone(msisdnMessage.getIphone());
        requestBean.setOperatorid(msisdnMessage.getOperatorid());
        requestBean.setOperatorType(msisdnMessage.getOperatorType());
        requestBean.setZid(msisdnMessage.getZid());
        // 查询流量
        try {
            requestBean = restTemplate
                    .postForEntity("http://112.74.57.55:8761/ctcc/queryTraffic", requestBean, UtilBean.class)
                    .getBody();
        } catch (Exception e) {
            log.error("物联网卡ID[{}]查询总流量出错,{}", msisdnMessage.getCardid(), e.getMessage());
        }

        if (StringUtils.isNotBlank(requestBean.getAnalyze())) {
            // 实际使用量
            double subResult = DecimalTools.sub(requestBean.getAnalyze(), "1.00");
            // 开始使用流量，更新数据库套餐表的开始时间和结束时间
            if (DecimalTools.compareTo(subResult, 0d) > -1) {
                log.info("物联网卡ID[{}]移动已使用量[{} kb],进行套餐包激活", requestBean.getCardId(),
                        requestBean.getAnalyze());
                databaseService.flashSlientStatus(requestBean.getCardId());
            }

        }

    }
}
