package org.com.coolfish.common.webinterface.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.com.coolfish.common.message.MsisdnMessage;
import org.com.coolfish.common.model.CMCCOperator;
import org.com.coolfish.common.webinterface.cmcc.xml.AssemblyCMCCXml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * @author LINGHUI
 * @desc 中国移动http 请求API
 */
@Service
public class CMCCRequestService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AssemblyCMCCXml cmccXML;

    @Autowired
    private RequestService requestService;
    
    private final static String CMCCURL="http://221.178.251.182:80/internet_surfing";
    private JsonParser parser = new JsonParser();
    public String queryStauts(CMCCOperator operator, MsisdnMessage msisdnMessage) {
        // 合成请求的xml数据
        String xml = cmccXML.queryStatus(operator.getAppid(), operator.getToken(), operator.getGroupid(), msisdnMessage.getTel(), String.valueOf(new Date().getTime()));
        String result = requestService.sendRequest("移动号码状态查询", msisdnMessage.getTel(), CMCCURL, xml);
    
        try {
            // 创建JsonObject对象
            JsonObject object = (JsonObject) parser.parse(result);
            // 得到为json的数组
            JsonArray contentJsonArray = object.get("content").getAsJsonArray();
            JsonObject contentJsonObject = contentJsonArray.get(0).getAsJsonObject();
            // 状态描述 1:正常（1/2） 2:半停（3） 3:全停状态（4/8/9）
            String status = contentJsonObject.get("status").getAsString();
            return status;
        } catch (Exception e) {
            logger.error("移动号码状态查询,解析查询号码[{}]响应消息出错, json-->{}", msisdnMessage.getTel(), result);
        }
      return null;
    }

    // 流量使用量查询
    public String queryTraffic(CMCCOperator operator, MsisdnMessage msisdnMessage) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String cycle = formatter.format(currentTime);
        // 合成请求的xml数据
        String xml = cmccXML.queryDankaXml(operator.getAppid(), operator.getToken(), msisdnMessage.getTel(), cycle, String.valueOf(new Date().getTime()));
          return getCumulateValue(msisdnMessage.getTel(), xml);
    }

    // 请求每月使用量 解析xml数据得到流量使用量
    public String getCumulateValue(String tel, String xml) {
        String result = requestService.sendRequest("移动号码使用量查询", tel, CMCCURL, xml);
          try {
            String cumulate_value = null;
             JsonObject object = (JsonObject) parser.parse(result);
            // 得到为json的数组
            JsonArray response = object.get("response").getAsJsonArray();
            JsonObject response1 = response.get(0).getAsJsonObject();
            String resp_code = response1.get("resp_code").getAsString();
            if ("0000".equals(resp_code)) {
                // 请求数据成功
                JsonArray contentJsonArray = object.get("content").getAsJsonArray();
                JsonObject contentJsonObject = contentJsonArray.get(0).getAsJsonObject();
                try {
                    JsonArray poollist = contentJsonObject.get("poollist").getAsJsonArray();
                    Long cumulate_value_cnt = 0L;
                    for (int i = 0; i < poollist.size(); i++) {
                        JsonObject poolJsonObject = poollist.get(i).getAsJsonObject();
                        JsonArray infoJsonArray = poolJsonObject.get("poolinfo").getAsJsonArray();
                        JsonObject infoJsonObject = infoJsonArray.get(0).getAsJsonObject();
                        cumulate_value = infoJsonObject.get("cumulate_value").getAsString();
                        if (StringUtils.isNotBlank(cumulate_value)) {
                            cumulate_value_cnt = cumulate_value_cnt + Long.parseLong(cumulate_value);
                        }
                    }
                    return String.valueOf(cumulate_value_cnt);
                } catch (Exception e) {
                    JsonArray prodlist = contentJsonObject.get("prodlist").getAsJsonArray();
                    Long cumulate_value_cnt = 0L;
                    for (int i = 0; i < prodlist.size(); i++) {
                        JsonObject prodJsonObject = prodlist.get(i).getAsJsonObject();
                        JsonArray infoJsonArray = prodJsonObject.get("prodinfo").getAsJsonArray();
                        JsonObject infoJsonObject = infoJsonArray.get(0).getAsJsonObject();
                        cumulate_value = infoJsonObject.get("cumulate_value").getAsString();
                        if (StringUtils.isNotBlank(cumulate_value)) {
                            cumulate_value_cnt = cumulate_value_cnt + Long.parseLong(cumulate_value);
                        }
                    }
                    return String.valueOf(cumulate_value_cnt);
                }
            } else {
                logger.error("移动号码[{}]使用量查询操作出错, json-->{}", tel, result);
            }
        } catch (Exception e) {

            logger.error("移动号码[{}]使用量查询响应消息解析出错, json-->{}", tel, result);
        }

        return null;
    }

    
    public boolean disabledNumber(CMCCOperator operator, String tel) {
        // 合成停机请求xml
        String xml =  cmccXML.tfjActionXml2(operator.getAppid(), operator.getToken(),operator.getGroupid(),tel, "1", "2");
               
        // 传入合成结果 得到停机返回结果
        return getDWResp(tel, xml);
    }

    // 移动停机操作
    public boolean getDWResp(String tel, String xml) {
        String result = requestService.sendRequest("移动号码停机操作", tel, CMCCURL, xml);
          try {
             JsonObject object = (JsonObject) parser.parse(result);
            // 得到为json的数组
            JsonArray contentJsonArray = object.get("content").getAsJsonArray();
            JsonObject contentJsonObject = contentJsonArray.get(0).getAsJsonObject();
            String ret_code = contentJsonObject.get("ret_code").getAsString();
            // 0：成功；1：失败
            if ("0".equals(ret_code)) {
                logger.info("移动号码[{}]停机操作成功, xml --> {}", tel, result);
                return true;
            } else {
                String ret_desc = contentJsonObject.get("ret_desc").getAsString();
                int exist = ret_desc.indexOf("与未完成的主动停机业务冲突");
                if (exist != -1) {
                    logger.info("移动号码[{}]停机操作与未完成的主动停机业务冲突,json --> {}", tel, result);
                    return true;
                } else {
                    logger.error("移动号码[{}]停机操作出错, json-->{}", tel,  result);
                }
            }
        } catch (Exception e) {
            logger.error("移动号码[{}]解析停机操作响应消息出错,json-->{}", tel, result);
        }

        return false;
    }

}
