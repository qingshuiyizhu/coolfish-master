package org.com.coolfish.service;

import org.com.coolfish.common.database.entity.KuyuCard;
import org.com.coolfish.common.model.DisabledBean;
import org.com.coolfish.common.model.UtilBean;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ActionService {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private RestTemplate restTemplate;

    private static final int CMCC = 1;

    private static final int CTCC = 2;

    public void test(int i) {

        UtilBean requestbean = new UtilBean();
        requestbean.setCardId(911248);
        requestbean.setIphone("1064973791357");
        requestbean.setOperatorid(219);
        requestbean.setZid(0);

        UtilBean result = restTemplate
                .postForEntity("http://114.55.132.207:8089/ctcc/queryStatus", requestbean, UtilBean.class)
                .getBody();
        System.out.println("Number" + i + result.toString());

    }

    public void test1(int i) {

        UtilBean requestbean = new UtilBean();
        requestbean.setCardId(227280);
        requestbean.setIphone("1064789800426");
        requestbean.setOperatorid(118);
        requestbean.setZid(0);

        UtilBean result = restTemplate
                .postForEntity("http://114.55.132.207:8089/cmcc/queryTraffic", requestbean, UtilBean.class)
                .getBody();
        System.out.println("Number" + i + result.toString());

    }

    public void test2(int i) {
        DisabledBean disabledBean = new DisabledBean();
        disabledBean.setActionReason("空套餐停机");
        disabledBean.setSource("流量监控");
        disabledBean.setCardId(980046);
        disabledBean.setIphone("1440030565224");
        disabledBean.setOperatorid(202);
        disabledBean.setZid(0);
        disabledBean.setOperatorType(1);
        disabledBean.setOprtype("1");//
        disabledBean.setReason("2");//

        UtilBean result = restTemplate.postForEntity("http://114.55.132.207:8089/cmcc/disabledNumber",
                disabledBean, DisabledBean.class).getBody();
        System.out.println("Number" + i + result.toString());

    }

    public void stop(KuyuCard card, String userflow, double rate, String actionReason) {
        // 流量使用超过99%，进行停机
        DisabledBean disabledBean = new DisabledBean();
        disabledBean.setActionReason(actionReason);
        disabledBean.setSource("流量监控");
        disabledBean.setCardId(card.getId());
        disabledBean.setIphone(card.getTel());
        disabledBean.setOperatorid(card.getOperatorid());
        disabledBean.setZid(card.getZid());
        disabledBean.setOperatorType(card.getOperator_type());
        disabledBean.setOprtype("1");//
        disabledBean.setReason("2");//
        if (CMCC == card.getOperator_type()) {

            rabbitTemplate.convertAndSend("switch-cmcc", JSON.toJSONString(disabledBean).toString());
            log.info("移动物联网卡ID[{}]套餐总量[{} kb], 已使用量[{} kb], 使用率[{}%],传到switch-cmcc队列进行停机操作{}", card.getId(),
                    String.valueOf(card.getSumflow()), userflow, rate * 100,
                    JSON.toJSONString(disabledBean).toString());
        } else if (CTCC == card.getOperator_type()) {
            rabbitTemplate.convertAndSend("switch-ctcc", JSON.toJSONString(disabledBean).toString());
            log.info("电信物联网卡ID[{}]套餐总量[{} kb], 已使用量[{} kb], 使用率[{}%],传到switch-ctcc队列进行停机操作{}", card.getId(),
                    String.valueOf(card.getSumflow()), userflow, rate * 100,
                    JSON.toJSONString(disabledBean).toString());
        }
    }

    @Async
    public void stopMonthlyNullSumFlow(KuyuCard kuyuCard) {
        UtilBean requestbean = new UtilBean();
        requestbean.setCardId(kuyuCard.getId());
        requestbean.setIphone(kuyuCard.getTel());
        requestbean.setOperatorid(kuyuCard.getOperatorid());
        requestbean.setZid(kuyuCard.getZid());
        if (kuyuCard.getOperator_type() == 1) {
            try {
                requestbean = restTemplate.postForEntity("http://114.55.132.207:8761/cmcc/queryStatus",
                        requestbean, UtilBean.class).getBody();
            } catch (Exception e) {
                log.error("请求http://114.55.132.207:8761/cmcc/queryStatus发生异常[{}]", e);
            }
            if ("1".equals(requestbean.getResultCode())) {// 正常，停机
                DisabledBean disabledBean = new DisabledBean();
                disabledBean.setActionReason("当月未订购套餐");
                disabledBean.setSource("流量监控");
                disabledBean.setCardId(kuyuCard.getId());
                disabledBean.setIphone(kuyuCard.getTel());
                disabledBean.setOperatorid(kuyuCard.getOperatorid());
                disabledBean.setZid(kuyuCard.getZid());
                disabledBean.setOperatorType(kuyuCard.getOperator_type());
                disabledBean.setOprtype("1");//
                disabledBean.setReason("2");//
                try {
                    disabledBean = restTemplate
                            .postForEntity("http://114.55.132.207:8761/cmcc/disabledNumber", disabledBean,
                                    DisabledBean.class)
                            .getBody();
                } catch (Exception e) {
                    log.error("请求http://114.55.132.207:8761/cmcc/disabledNumber发生异常[{}]", e);
                }
            }
        } else if (kuyuCard.getOperator_type() == 2) {
            try {
                requestbean = restTemplate.postForEntity("http://112.74.57.55:8761/ctcc/queryStatus",
                        requestbean, UtilBean.class).getBody();
            } catch (Exception e) {
                log.error("请求http://112.74.57.55:8761/ctcc/queryStatus发生异常[{}]", e);
            }
            if ("1".equals(requestbean.getResultCode())) {// 正常，停机
                log.info("物联网卡ID[{}],[{}]当月未订购套餐，状态在用，进行停机操作", kuyuCard.getId(), kuyuCard.getTel());
                DisabledBean disabledBean = new DisabledBean();
                disabledBean.setActionReason("当月未订购套餐");
                disabledBean.setSource("流量监控");
                disabledBean.setCardId(kuyuCard.getId());
                disabledBean.setIphone(kuyuCard.getTel());
                disabledBean.setOperatorid(kuyuCard.getOperatorid());
                disabledBean.setZid(kuyuCard.getZid());
                disabledBean.setOperatorType(kuyuCard.getOperator_type());
                disabledBean.setOprtype("1");//
                disabledBean.setReason("2");//
                try {
                    disabledBean = restTemplate
                            .postForEntity("http://112.74.57.55:8761/ctcc/disabledNumber", disabledBean,
                                    DisabledBean.class)
                            .getBody();
                } catch (Exception e) {
                    log.error("请求http://112.74.57.55:8761/ctcc/disabledNumber发生异常[{}]", e);
                }
            }
        }
    }

    public void startMonthlyHaveSumFlowScheduler(KuyuCard kuyuCard) {
        UtilBean requestbean = new UtilBean();
        requestbean.setCardId(kuyuCard.getId());
        requestbean.setIphone(kuyuCard.getTel());
        requestbean.setOperatorid(kuyuCard.getOperatorid());
        requestbean.setZid(kuyuCard.getZid());
        if (kuyuCard.getOperator_type() == 1) {
            requestbean = restTemplate
                    .postForEntity("http://114.55.132.207:8761/cmcc/queryStatus", requestbean, UtilBean.class)
                    .getBody();
            if (!"1".equals(requestbean.getResultCode()) && !"-1".equals(requestbean.getResultCode())) {//
                DisabledBean disabledBean = new DisabledBean();
                disabledBean.setActionReason("已订购当月套餐");
                disabledBean.setSource("流量监控");
                disabledBean.setCardId(kuyuCard.getId());
                disabledBean.setIphone(kuyuCard.getTel());
                disabledBean.setOperatorid(kuyuCard.getOperatorid());
                disabledBean.setZid(kuyuCard.getZid());
                disabledBean.setOperatorType(kuyuCard.getOperator_type());
                disabledBean.setOprtype("2");//
                disabledBean.setReason("7");//

                disabledBean = restTemplate.postForEntity("http://114.55.132.207:8761/cmcc/disabledNumber",
                        disabledBean, DisabledBean.class).getBody();

            }
        } else if (kuyuCard.getOperator_type() == 2) {
            requestbean = restTemplate
                    .postForEntity("http://112.74.57.55:8761/ctcc/queryStatus", requestbean, UtilBean.class)
                    .getBody();

            if (!"1".equals(requestbean.getResultCode()) && !"-1".equals(requestbean.getResultCode())) {// 正常，停机
                DisabledBean disabledBean = new DisabledBean();
                disabledBean.setActionReason("已订购当月套餐");
                disabledBean.setSource("流量监控");
                disabledBean.setCardId(kuyuCard.getId());
                disabledBean.setIphone(kuyuCard.getTel());
                disabledBean.setOperatorid(kuyuCard.getOperatorid());
                disabledBean.setZid(kuyuCard.getZid());
                disabledBean.setOperatorType(kuyuCard.getOperator_type());
                disabledBean.setOprtype("2");//
                disabledBean.setReason("7");//
                disabledBean = restTemplate.postForEntity("http://112.74.57.55:8761/ctcc/disabledNumber",
                        disabledBean, DisabledBean.class).getBody();
            }
        }

    }

}
