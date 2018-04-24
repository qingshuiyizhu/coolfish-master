package org.com.coolfish.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.com.coolfish.config.CMCCParam;
import org.com.coolfish.ctcc.utils.XmlTool;
import org.com.coolfish.entity.MqJson;
import org.com.coolfish.entity.OperatorText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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

    // 移动流量监控方法
    public void LiuLiangControl(OperatorText operator, MqJson json) {
        long startTime, endTime;
        startTime = System.currentTimeMillis(); // 获取开始时间
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String date = formatter.format(currentTime);
        String xml = cmccService.queryDkllcx(json.getTel(), date, date + "_" + json.getTel(),
                operator.getAppid(), operator.getToken());
         String result = restTemplate.postForEntity(param.getUrl(), xml, String.class).getBody();
         //返回的使用量
        double CumulateValue = (getCumulateValue(result) / json.getPer());
        //如果沒有得到流量，重试
        if(-1 == CumulateValue) {
            for(int i=0;i<3;i++) {
                result = restTemplate.postForEntity(param.getUrl(), xml, String.class).getBody();
                CumulateValue = (getCumulateValue(result) / json.getPer());
                if(CumulateValue>0) {
                    break;
                }
            }
        }
         if(-1 == CumulateValue) {
            logger.info("移动号码流量号码获取失败，扔回队列cmcc-monthly85" + json.getTel()); 
            // 流程查询失败，扔回队列
            rabbitTemplate.convertAndSend("cmcc-monthly85", JSON.toJSONString(json) + new Date());
          }
        // 处理沉默期 
        if (CumulateValue > 0 && json.getSlient() == 1) {
            logger.info("移动号码处理沉默期:"+json.getTel()); 
          kuyuCardService.upTime(json.getTel());
        }

        if (CumulateValue > json.getSumflow()) {
            // 停机操作
            logger.info("移动号码停机操作：" + json.getTel());
            xml = cmccService.tfjAction(json.getTel(), "1", "流量使用完停机", operator.getAppid(), operator.getToken(),
                    operator.getGroupid());
            TingJi(json, xml, 0);
            return;
        } else if (0.85 <= (CumulateValue / json.getSumflow())) {
            // 传到85队列
            // #更新使用量
            json.setUseflow(CumulateValue / json.getPer());
            rabbitTemplate.convertAndSend("cmcc-monthly85", JSON.toJSONString(json) + new Date());
            logger.info("移动号码流量使用率达到85%，传到cmcc-monthly85：" + json.getTel());
        } else {
            logger.info("移动号码流量使用正常，不执行任何操作：" + json.getTel());
        }
        endTime = System.currentTimeMillis(); // 获取结束时间
        logger.info("移动流量监控：" + json.getTel() + "消耗时间：" + (endTime - startTime) + "ms");
    }

    // 移动停机操作 
    //最小重试次数
    //重试阈值
    private MqJson TingJi(MqJson json, String xml, int i) {
        String result;
        result = restTemplate.postForEntity(param.getUrl(), xml, String.class).getBody();
        logger.info("停机操作：" + json.getTel() + ",返回结果：" + result);
        int status = getStatus(result);
        json.setRetry(json.getRetry() + 1);
        if (1 != status) {
            // 停机成功 ，更新到数据库
            kuyuCardService.editStatus(status, json.getTel());
      } else if (i < param.getEvmax() && json.getRetry() < param.getRemax()) {
            // 回掉函数
            return TingJi(json, xml, i++);
        } else {
            // 重试次数达到最大，传到email队列
            rabbitTemplate.convertAndSend("email", json.getTel() + new Date());
        }
        return json;
    }

    
    // 解析xml数据得到流量使用量
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
            logger.error("移动流量卡套餐使用信息为空：，请求的xml为" + xml);
            return -1;
        }
        return cumulate_value;
    }

    // 停机操作返回值
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
            logger.error("移动流量卡停机操作出错：返回的xml为" + xml);
            return -1;
        }
        return value;
    }
    
    
}
