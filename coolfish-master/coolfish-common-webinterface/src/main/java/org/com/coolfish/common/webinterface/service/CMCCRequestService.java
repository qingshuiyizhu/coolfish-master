package org.com.coolfish.common.webinterface.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.com.coolfish.common.model.CMCCOperator;
import org.com.coolfish.common.model.DisabledBean;
import org.com.coolfish.common.model.UtilBean;
import org.com.coolfish.common.webinterface.cmcc.xml.AssemblyCMCCXml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author LINGHUI
 * @desc 中国移动http 请求API
 */
@Slf4j
@Service
public class CMCCRequestService {
    @Autowired
    private AssemblyCMCCXml cmccXML;

    @Autowired
    private RequestService requestService;

    private final static String CMCCURL = "http://221.178.251.182:80/internet_surfing";

    private JsonParser parser = new JsonParser();

    public UtilBean queryStatus(CMCCOperator operator, UtilBean requestBean) {
        // 合成请求的xml数据
        String xml = cmccXML.queryStatus(operator.getAppid(), operator.getToken(), operator.getGroupid(),
                requestBean.getIphone(), String.valueOf(new Date().getTime()));
        String result = requestService.sendRequest("移动号码状态查询", requestBean.getCardId(), CMCCURL, xml);

        try {
            // 创建JsonObject对象
            JsonObject object = (JsonObject) parser.parse(result);
            // 得到为json的数组
            JsonArray contentJsonArray = object.get("content").getAsJsonArray();
            JsonObject contentJsonObject = contentJsonArray.get(0).getAsJsonObject();
            // 状态描述 1:正常（1/2） 2:半停（3） 3:全停状态（4/8/9）
            String resultCode = contentJsonObject.get("status").getAsString();
            requestBean.setResultCode(resultCode);
            if ("1".equals(resultCode)) {
                requestBean.setResultMsg("正常");
            } else if ("2".equals(resultCode)) {
                requestBean.setResultMsg("半停");
            } else {
                requestBean.setResultMsg("全停");
            }

        } catch (Exception e) {
            log.error("移动物联网卡ID[{}]状态查询,解析响应消息出错, json-->{}", requestBean.getCardId(), result);
            requestBean.setResultCode("-1");
            requestBean.setResultMsg("状态查询响应消息解析异常");
        }
        return requestBean;
    }

    // 流量使用量查询
    public UtilBean queryTraffic(CMCCOperator operator, UtilBean requestBean) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String cycle = formatter.format(currentTime);
        // 合成请求的xml数据
        String xml = cmccXML.queryDankaXml(operator.getAppid(), operator.getToken(), requestBean.getIphone(),
                cycle, String.valueOf(new Date().getTime()));
        return getCumulateValue(requestBean, xml);
    }

    // 请求每月使用量 解析xml数据得到流量使用量
    public UtilBean getCumulateValue(UtilBean requestBean, String xml) {
        String result = requestService.sendRequest("移动号码使用量查询", requestBean.getCardId(), CMCCURL, xml);
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
                Long cumulate_value_cnt = 0L;

                if (contentJsonObject.get("poollist").toString().length() > 10) {
                    JsonArray poollist = contentJsonObject.get("poollist").getAsJsonArray();
                    for (int i = 0; i < poollist.size(); i++) {
                        JsonObject poolJsonObject = poollist.get(i).getAsJsonObject();
                        JsonArray infoJsonArray = poolJsonObject.get("poolinfo").getAsJsonArray();
                        JsonObject infoJsonObject = infoJsonArray.get(0).getAsJsonObject();
                        cumulate_value = infoJsonObject.get("cumulate_value").getAsString();
                        if (StringUtils.isNotBlank(cumulate_value)) {
                            cumulate_value_cnt = cumulate_value_cnt + Long.parseLong(cumulate_value);
                        }
                    }
                }
                if (contentJsonObject.get("prodlist").toString().length() > 10) {
                    JsonArray prodlist = contentJsonObject.get("prodlist").getAsJsonArray();
                    for (int i = 0; i < prodlist.size(); i++) {
                        JsonObject prodJsonObject = prodlist.get(i).getAsJsonObject();
                        JsonArray infoJsonArray = prodJsonObject.get("prodinfo").getAsJsonArray();
                        JsonObject infoJsonObject = infoJsonArray.get(0).getAsJsonObject();
                        cumulate_value = infoJsonObject.get("cumulate_value").getAsString();
                        if (StringUtils.isNotBlank(cumulate_value)) {
                            cumulate_value_cnt = cumulate_value_cnt + Long.parseLong(cumulate_value);
                        }
                    }
                }
                requestBean.setAnalyze(String.valueOf(cumulate_value_cnt));
            } else {
                requestBean.setResultCode("-1");
                requestBean.setResultMsg("使用量查询查询失败");
                log.warn("移动物联网卡ID[{}]使用量查询查询失败, json-->{}", requestBean.getCardId(), result);
            }
        } catch (Exception e) {
            requestBean.setResultCode("-1");
            requestBean.setResultMsg("使用量查询响应消息解析异常");
            log.error("移动物联网卡ID[{}]使用量查询响应消息解析异常, json-->{}", requestBean.getCardId(), result);
        }
        return requestBean;
    }

    public DisabledBean disabledNumber(CMCCOperator operator, DisabledBean disabledBean) {
        // 合成停复机请求xml
        String xml = cmccXML.tfjActionXml(operator.getAppid(), operator.getToken(), operator.getGroupid(),
                disabledBean.getIphone(), disabledBean.getOprtype(), disabledBean.getReason());

        String action = "移动号码停复机操作类型-未知";
        if ("1".equals(disabledBean.getOprtype())) {
            action = "移动号码停复机操作类型-停机";
        } else if ("2".equals(disabledBean.getOprtype())) {
            action = "移动号码停复机操作类型-复机";
        }
        // 传入合成结果 得到停机返回结果
        String result = requestService.sendRequest(action, disabledBean.getCardId(), CMCCURL, xml);
        try {
            JsonObject object = (JsonObject) parser.parse(result);
            // 得到为json的数组
            JsonArray contentJsonArray = object.get("content").getAsJsonArray();
            JsonObject contentJsonObject = contentJsonArray.get(0).getAsJsonObject();
            String resultCode = contentJsonObject.get("ret_code").getAsString();
            String resultMsg = contentJsonObject.get("ret_desc").getAsString();
            try {
                String serialNumber = contentJsonObject.get("orderid").getAsString();
                disabledBean.setSerialNumber(serialNumber);

            } catch (Exception e) {
                disabledBean.setSerialNumber("  ");
            }
            disabledBean.setResultMsg(resultMsg);
            disabledBean.setResultCode(resultCode);// 1、 正常（1/2） 2、 半停（3） 3、 全停状态（4/8/9）

        } catch (Exception e) {
            log.error("移动物联网卡ID[{}]解析复机操作响应消息出错,json-->{}", disabledBean.getCardId(), result);
            disabledBean.setResultCode("-1");
            disabledBean.setResultMsg("解析第三响应消息发生异常");
        }
        return disabledBean;
    }

}
