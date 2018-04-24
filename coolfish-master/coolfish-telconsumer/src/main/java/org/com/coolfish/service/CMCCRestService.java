package org.com.coolfish.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.com.coolfish.cha.utils.XmlTool;
import org.com.coolfish.config.CMCCParam;
import org.com.coolfish.entity.KuyuCard;
import org.com.coolfish.entity.MqJson;
import org.com.coolfish.entity.OperatorText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * @author LINGHUI
 * @desc 中国移动http 请求API
 */
@Scope("prototype")
@Service
public class CMCCRestService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CMCCService cmccService;

    @Autowired
    private KuyuCardService kuyuCardService;

    @Autowired
    private CMCCParam param;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Async
    public void LiuLiangControl(OperatorText operator, MqJson json) {
        long startTime, endTime;
        startTime = System.currentTimeMillis(); // 获取开始时间
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String date = formatter.format(currentTime);

        String xml = cmccService.DKLLCXQuery(json.getTel(), date, date + "_" + json.getTel(),
                operator.getAppid(), operator.getToken());
        String result = restTemplate.postForEntity(param.getUrl(), xml, String.class).getBody();
        double CumulateValue = (getCumulateValue(result) / json.getPer());
        if (CumulateValue > json.getSumflow()) {
            // 停机操作
            logger.info("停机操作：" + json.getTel());
            xml = cmccService.TFJQuery(json.getTel(), "1", "空套餐停机", operator.getAppid(), operator.getToken(),
                    operator.getGroupid());
            TingJi(json, xml, 0);
            return;
        } else if (0.85 <= (CumulateValue / json.getSumflow())) {
            // 传到85队列
            // #更新使用量
            json.setUseflow(CumulateValue / json.getPer());
            rabbitTemplate.convertAndSend("cmcc-monthly85", JSON.toJSONString(json));
        } else {
            logger.info("无操作：" + json.getTel());
        }
        endTime = System.currentTimeMillis(); // 获取结束时间
        logger.info("流量监控：" + json.getTel() + "消耗时间：" + (endTime - startTime) + "ms");
    }

    private MqJson TingJi(MqJson json, String xml, int i) {
        String result;
        result = restTemplate.postForEntity(param.getUrl(), xml, String.class).getBody();
        logger.info("停机操作：" + json.getTel() + ",返回结果：" + result);
        int status = getStatus(result);
        json.setRetry(json.getRetry() + 1);
        if (1 != status) {
            // 停机成功 ，更新到数据库
          //  kuyuCardService.upStatus();
            
            
        } else if (i < 3 && json.getRetry() < 10) {
            //回掉函数
            return TingJi(json, xml, i++);
        } else {
            // 发送邮件

        }
        return json;
    }

    /*
     * 3.物联网集团成员停复机
     * 
     * 请求参数 service_number ：物联网号码(取值是数字串) cycle: 查询月份 (yyyymmdd) req_seq:流水号
     */
    @Async
    public void NUllLiuLiangTFJ(Integer opid, String appid, String token, String groupid, String password) {
        List<KuyuCard> list = kuyuCardService.findInA();
        for (int i = 0; i < list.size(); i++) {
            String tel = list.get(i).getTel();
            String xml = cmccService.DKLLCXQuery(tel, "201804", "0_" + i, appid, token);
            String result = restTemplate.postForEntity(param.getUrl(), xml, String.class).getBody();
            int leftValue = 0;
            // int leftValue = getLeftValue(result);
            if (leftValue > 0) {
                logger.info("停机操作：" + tel);
                xml = cmccService.TFJQuery(tel, "1", "空套餐停机", appid, token, groupid);
                result = restTemplate.postForEntity(param.getUrl(), xml, String.class).getBody();
                logger.info("停机操作：" + tel + ",返回结果：" + result);
            } else {
                logger.info("无操作：" + tel);
            }
        }

    }

    // 得到流量使用量
    public double getCumulateValue(String xml) {
        String str = "";
        double cumulate_value = -1;
        try {
            str = XmlTool.documentToJSONObject(xml).toJSONString();
            System.out.println(str);
            // 创建JSON解析器
            JsonParser parser = new JsonParser();
            // 创建JsonObject对象
            JsonObject object = (JsonObject) parser.parse(str);
            // 得到为json的数组
            JsonArray content = object.get("content").getAsJsonArray();
            JsonObject content1 = content.get(0).getAsJsonObject();
            JsonArray prodlist = content1.get("prodlist").getAsJsonArray();
            JsonObject prodlist1 = prodlist.get(0).getAsJsonObject();

            JsonArray prodinfo = prodlist1.get("prodinfo").getAsJsonArray();
            JsonObject prodinfo1 = prodinfo.get(0).getAsJsonObject();
            cumulate_value = prodinfo1.get("cumulate_value").getAsDouble();
        } catch (Exception e) {
            logger.error("流量卡套餐使用信息为空：，请求的xml为" + xml);
            return -1;
        }
        return cumulate_value;
    }

    // 得到流量使用量
    public int getStatus(String xml) {
        String str = "";
        int value = -1;
        try {
            str = XmlTool.documentToJSONObject(xml).toJSONString();
            System.out.println(str);
            // 创建JSON解析器
            JsonParser parser = new JsonParser();
            // 创建JsonObject对象
            JsonObject object = (JsonObject) parser.parse(str);
            // 得到为json的数组
            JsonArray content = object.get("content").getAsJsonArray();
            JsonObject content1 = content.get(0).getAsJsonObject();
            value = content1.get("status").getAsInt();
        } catch (Exception e) {
            logger.error("流量卡套餐使用信息为空：，请求的xml为" + xml);
            return -1;
        }
        return value;
    }
}
