package org.com.coolfish.lister.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.com.coolfish.common.database.service.KuyuAddPackageService;
import org.com.coolfish.common.message.MsisdnMessage;
import org.com.coolfish.common.model.DisabledBean;
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
public class QueryMonthlyService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private KuyuAddPackageService addPackageService;

    @Autowired
    private RestTemplate restTemplate;

    private final static int CMCC = 1;

    private final static int CTCC = 2;

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void handle(String message) {
        // 将String 转为MsisdnMessage对象
        log.info("月套餐流量状态查询队列(query-monthly)获取数据[{}]", message);
        MsisdnMessage msisdnMessage = JSON.parseObject(message, MsisdnMessage.class);
        long startTime = System.currentTimeMillis();
        log.info("物联网卡ID[{}]开始执行流量状态查询, 开始执行时间:{}, 时间戳:{}", msisdnMessage.getCardid(),
                sdf.format(new Date(startTime)), startTime);
        handleMessage(msisdnMessage);
        long endTime = System.currentTimeMillis();
        log.info("物联网卡ID[{}]结束执行流量状态查询, 结束执行时间:{}, 时间戳:{}, 耗时:{}ms", msisdnMessage.getCardid(),
                sdf.format(new Date(endTime)), endTime, (endTime - startTime));
    }

    // 处理监听到的数据
    @Async
    public void handleMessage(MsisdnMessage msisdnMessage) {
        if (CMCC == msisdnMessage.getOperatorType()) {
            log.info("物联网卡ID[{}]调用移动流量状态查询服务", msisdnMessage.getCardid());
            handleCMCCQuery(msisdnMessage);

        } else if (CTCC == msisdnMessage.getOperatorType())
            log.info("调用电信流量状态查询服务", msisdnMessage.getCardid());
        handleCTCCQuery(msisdnMessage);
    }

    public void handleCMCCQuery(MsisdnMessage msisdnMessage) {
        // 查询数据库的总流量
        long startTime = System.currentTimeMillis();
        log.info("移动号码[{}]开始查询数据库表里的总流量, 开始执行时间:{}, 时间戳:{}", msisdnMessage.getCardid(),
                sdf.format(new Date(startTime)), startTime);
        // 查询数据库中的总流量
        BigDecimal sumFlow = null;
        try {
            sumFlow = addPackageService.findMonthlySumFlows(msisdnMessage.getCardid());
        } catch (Exception e) {
            log.error("移动物联网卡ID[{}]查询总流量出错,{}", msisdnMessage.getCardid(), e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        log.info("移动物联网卡ID[{}]结束执行查询数据库表里的总流量, 结束执行时间:{}, 时间戳:{}, 耗时:{}ms", msisdnMessage.getCardid(),
                sdf.format(new Date(endTime)), endTime, (endTime - startTime));
        if (null != sumFlow) {
            msisdnMessage.setSumflow(String.valueOf(sumFlow));
        } else {
            msisdnMessage.setSumflow("1.00");
        }

        UtilBean requestBean = new UtilBean();
        requestBean.setCardId(msisdnMessage.getCardid());
        requestBean.setIccid(msisdnMessage.getIccid());
        requestBean.setIphone(msisdnMessage.getIphone());
        requestBean.setOperatorid(msisdnMessage.getOperatorid());
        requestBean.setOperatorType(msisdnMessage.getOperatorType());
        requestBean.setZid(msisdnMessage.getZid());

        requestBean = restTemplate
                .postForEntity("http://114.55.132.207:8761/cmcc/queryTraffic", requestBean, UtilBean.class)
                .getBody();

        double realApiflow = 0D;
        if (StringUtils.isNotBlank(requestBean.getAnalyze()) && !"-1".equals(requestBean.getResultCode())) {
            String nowApifow = requestBean.getAnalyze();
            // 实际使用量
            realApiflow = DecimalTools.div(nowApifow, msisdnMessage.getPer(), 2);
            msisdnMessage.setUseflow(String.valueOf(realApiflow));
        }
        // 查询状态
        requestBean = restTemplate
                .postForEntity("http://114.55.132.207:8761/cmcc/queryStatus", requestBean, UtilBean.class)
                .getBody();
        if (!"-1".equals(requestBean.getResultCode())) {
            String startusCode = requestBean.getResultCode();
            String cardStatus = requestBean.getResultMsg();
            msisdnMessage.setStartusCode(startusCode);
            msisdnMessage.setCardStatus(cardStatus);

        }

        // 使用量/总流量
        double rate = DecimalTools.div(msisdnMessage.getUseflow(), msisdnMessage.getSumflow(), 2);

        if (DecimalTools.compareTo(rate, 0.99d) > -1 && "1".equals(msisdnMessage.getStartusCode())) {

            // 调用停机方法
            DisabledBean disabledBean = new DisabledBean();
            disabledBean.setActionReason("月套餐流量用完");
            disabledBean.setSource("流量监控");
            disabledBean.setCardId(msisdnMessage.getCardid());
            disabledBean.setIphone(msisdnMessage.getIphone());
            disabledBean.setOperatorid(msisdnMessage.getOperatorid());
            disabledBean.setZid(msisdnMessage.getZid());
            disabledBean.setOperatorType(msisdnMessage.getOperatorType());
            disabledBean.setOprtype("1");//
            disabledBean.setReason("2");//
            disabledBean = restTemplate.postForEntity("http://114.55.132.207:8761/cmcc/disabledNumber",
                    disabledBean, DisabledBean.class).getBody();
            int exist = disabledBean.getResultMsg().indexOf("停机状态的用户不允许再停机");
            if ("0".equals(disabledBean.getResultCode()) || exist != -1) {
                // 成功，
                msisdnMessage.setCardStatus("停机");
                msisdnMessage.setStartusCode("4");
            }
        }
        // 数据同步到Redis缓存上
        redisService.set(String.valueOf(msisdnMessage.getCardid()),
                JSON.toJSONString(msisdnMessage).toString());
        log.info("物联网卡ID[{}]套餐总量[{} kb], 已使用量[{} kb], 使用率[{}%],信息更新到Redis缓存中{}", msisdnMessage.getCardid(),
                msisdnMessage.getSumflow(), msisdnMessage.getUseflow(), rate * 100,
                JSON.toJSONString(msisdnMessage).toString());
    }

    // 电信月套餐监控
    public void handleCTCCQuery(MsisdnMessage msisdnMessage) {
        // 查询数据库的总流量
        long startTime = System.currentTimeMillis();
        log.info("电信物联网卡ID[{}]开始查询数据库表里的总流量, 开始执行时间:{}, 时间戳:{}", msisdnMessage.getCardid(),
                sdf.format(new Date(startTime)), startTime);
        // 查询数据库中的总流量
        BigDecimal sumFlow = null;
        try {
            sumFlow = addPackageService.findMonthlySumFlows(msisdnMessage.getCardid());
        } catch (Exception e) {
            log.error("电信物联网卡ID[{}]查询总流量出错,{}", msisdnMessage.getCardid(), e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        log.info("电信物联网卡ID[{}]结束执行查询数据库表里的总流量, 结束执行时间:{}, 时间戳:{}, 耗时:{}ms", msisdnMessage.getCardid(),
                sdf.format(new Date(endTime)), endTime, (endTime - startTime));
        if (null != sumFlow) {
            msisdnMessage.setSumflow(String.valueOf(sumFlow));
        } else {
            msisdnMessage.setSumflow("1.00");
        }

        // 查询电信当月使用量
        UtilBean requestBean = new UtilBean();
        requestBean.setCardId(msisdnMessage.getCardid());
        requestBean.setIccid(msisdnMessage.getIccid());
        requestBean.setIphone(msisdnMessage.getIphone());
        requestBean.setOperatorid(msisdnMessage.getOperatorid());
        requestBean.setOperatorType(msisdnMessage.getOperatorType());
        requestBean.setZid(msisdnMessage.getZid());
        // 查询流量
        requestBean = restTemplate
                .postForEntity("http://112.74.57.55:8761/ctcc/queryTraffic", requestBean, UtilBean.class)
                .getBody();
        double realApiflow = 0D;
        if (StringUtils.isNotBlank(requestBean.getAnalyze())) {
            String nowApifow = requestBean.getAnalyze();
            // 实际使用量
            realApiflow = DecimalTools.div(nowApifow, msisdnMessage.getPer(), 2);
            msisdnMessage.setUseflow(String.valueOf(realApiflow));
        }
        // 查询状态
        requestBean = restTemplate
                .postForEntity("http://112.74.57.55:8761/ctcc/queryStatus", requestBean, UtilBean.class)
                .getBody();
        if (!"-1".equals(requestBean.getResultCode())) {
            String startusCode = requestBean.getResultCode();
            String cardStatus = requestBean.getResultMsg();
            msisdnMessage.setStartusCode(startusCode);
            msisdnMessage.setCardStatus(cardStatus);

        }
        // 使用量/总流量
        double rate = DecimalTools.div(msisdnMessage.getUseflow(), msisdnMessage.getSumflow(), 2);

        if (DecimalTools.compareTo(rate, 0.99d) > -1 && "1".equals(msisdnMessage.getStartusCode())) {
            // 调用停机方法
            DisabledBean disabledBean = new DisabledBean();
            disabledBean.setActionReason("月套餐流量用完");
            disabledBean.setSource("流量监控");
            disabledBean.setCardId(msisdnMessage.getCardid());
            disabledBean.setIphone(msisdnMessage.getIphone());
            disabledBean.setOperatorid(msisdnMessage.getOperatorid());
            disabledBean.setZid(msisdnMessage.getZid());
            disabledBean.setOperatorType(msisdnMessage.getOperatorType());
            disabledBean.setOprtype("1");//
            disabledBean.setReason("2");//
            disabledBean = restTemplate.postForEntity("http://112.74.57.55:8761/ctcc/disabledNumber",
                    disabledBean, DisabledBean.class).getBody();
            int exist = disabledBean.getResultMsg().indexOf("已存在停机");
            if ("0".equals(disabledBean.getResultCode()) || exist != -1) {
                // 成功，删除redis上的数据
                msisdnMessage.setCardStatus("停机");
                msisdnMessage.setStartusCode("4");
            }
        }
        // 数据同步到Redis缓存上
        redisService.set(String.valueOf(msisdnMessage.getCardid()),
                JSON.toJSONString(msisdnMessage).toString());
        // 忽略处理
        log.info("物联网卡ID[{}]套餐总量[{} kb], 已使用量[{} kb], 使用率[{}%],信息更新到Redis缓存中{}", msisdnMessage.getCardid(),
                msisdnMessage.getSumflow(), msisdnMessage.getUseflow(), rate * 100,
                JSON.toJSONString(msisdnMessage).toString());

    }
}
