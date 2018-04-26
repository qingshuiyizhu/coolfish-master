package org.com.coolfish.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.com.coolfish.config.CMCCParam;
import org.com.coolfish.entity.CMCCOperator;
import org.com.coolfish.entity.KuyuCardSimstate;
import org.com.coolfish.entity.KuyuFlowDetail;
import org.com.coolfish.message.MsisdnMessage;
import org.com.coolfish.util.DecimalTools;
import org.com.coolfish.util.XmlTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private KuyuCardSimstateService kuyuCardSimstateService;

    @Autowired
    private KuyuFlowDetailService kuyuFlowDetailService;

    public String sendRequest(String interfaceName, String tel, String xml) {
        logger.info("移动号码[{}]调用API接口[{}]请求消息, xml--> {}", tel, interfaceName, xml);
        long startTime = System.currentTimeMillis();
        String result = null;
        try {
            result = restTemplate.postForEntity(param.getUrl(), xml, String.class).getBody();
            logger.info("移动号码[{}]调用API接口[{}]响应消息, 耗时:{}, xml--> {}", tel, interfaceName,
                    (System.currentTimeMillis() - startTime), result);
        } catch (Exception e) {
            logger.error("移动号码[{}]调用API接口[{}]发生异常错误, 耗时:{}, xml--> {}", tel, interfaceName,
                    (System.currentTimeMillis() - startTime), result);
        }

        return result;
    }

    /**
     * 移动流量监控方法
     * 
     * @param operator
     * @param msisdnMessage 1.通过API查询当月的月套餐流量使用量 2.
     */
    public void handle(CMCCOperator operator, MsisdnMessage message) {
        // 1. 查询号码使用量
        String nowApiflow = queryTraffic(operator, message);
        double realApiflow = 0;
        // 2.1使用量为null 查询失败
        if (nowApiflow == null) {
            logger.warn("移动号码[{}]查询使用量失败，传回队列cmcc-monthly85", message.getTel());
            // 流程查询失败，传回队列cmcc-monthly85
            rabbitTemplate.convertAndSend("cmcc-monthly85", JSON.toJSONString(message).toString());
        } else {
            // 2.2查询成功 除于流量比例 得到实际使用量
            realApiflow = DecimalTools.div(nowApiflow, message.getPer(), 2);

            // 总流量-使用量 <=0
            double subResult = DecimalTools.sub(message.getSumflow(), Double.toString(realApiflow));
            // 使用量/总流量
            double result85 = DecimalTools.div(Double.toString(realApiflow), message.getSumflow(), 2);

            java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            logger.info("移动号码[{}]的套餐总量[{} kb], 已使用量[{} kb], 使用率[{}%]", message.getTel(), message.getSumflow(),
                    nf.format(realApiflow), result85 * 100);

            if (DecimalTools.compareTo(subResult, 0d) < 1) {
                // 获取号码状态
                String status = queryStauts(operator, message);
                if (StringUtils.isNotBlank(status)) {
                    if ("1".equals(status)) {
                        // 停机操作
                        logger.info("移动号码[{}]进行停机操作", message.getTel());
                        disabledNumberController(operator, message);
                    } else {
                        String statusDesc = ("2".equals(status)) ? "半停" : "全停";
                        logger.info("移动号码[{}]状态:[{}], 忽略停机操作", message.getTel(), statusDesc);
                    }
                }
            } else if (DecimalTools.compareTo(result85, 0.85d) > -1) {
                // 传到电信月套餐使用率达85的队列
                // #更新使用量
                message.setUseflow(Double.toString(realApiflow));
                rabbitTemplate.convertAndSend("cmcc-monthly85", JSON.toJSONString(message).toString());
                logger.info("移动号码[{}]流量使用率达到85%，传到cmcc-monthly85队列：{}", message.getTel(),
                        JSON.toJSONString(message).toString());
            }
        }

        // 进行每日流量使用量记录写入数据库 ,判断是否在记录时间内
        boolean isInsert = isInDate(new Date(), param.getLogstartTime(), param.getLogendTime());
        if (isInsert) {
            EveryDayLogWriter(message, realApiflow);
        }

    }

    private String queryStauts(CMCCOperator operator, MsisdnMessage msisdnMessage) {
        // 合成请求的xml数据
        String xml = cmccService.queryStatus(msisdnMessage.getTel(), System.currentTimeMillis() + "",
                operator.getAppid(), operator.getToken(), operator.getGroupid());
        String result = sendRequest("查询号码状态", msisdnMessage.getTel(), xml);
        String str = null;
        try {
            str = XmlTool.documentToJSONObject(result).toJSONString();
            // 创建JSON解析器
            JsonParser parser = new JsonParser();
            // 创建JsonObject对象
            JsonObject object = (JsonObject) parser.parse(str);
            // 得到为json的数组
            JsonArray contentJsonArray = object.get("content").getAsJsonArray();
            JsonObject contentJsonObject = contentJsonArray.get(0).getAsJsonObject();
            // 状态描述 1:正常（1/2） 2:半停（3） 3:全停状态（4/8/9）
            String status = contentJsonObject.get("status").getAsString();
            return status;
        } catch (Exception e) {
            logger.error("移动号码[{}]解析查询号码状态响应消息出错, json-->{},xml --> {}", msisdnMessage.getTel(), str, result);
        }

        return null;
    }

    // 流量使用量查询
    public String queryTraffic(CMCCOperator operator, MsisdnMessage msisdnMessage) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String date = formatter.format(currentTime);
        // 合成请求的xml数据
        String xml = cmccService.queryDankaXml(msisdnMessage.getTel(), date,
                date + "_" + msisdnMessage.getTel(), operator.getAppid(), operator.getToken());

        return getCumulateValue(msisdnMessage.getTel(), xml);
    }

    // 请求每月使用量 解析xml数据得到流量使用量
    public String getCumulateValue(String tel, String xml) {
        String result = sendRequest("查询使用量", tel, xml);
        String str = null;
        try {
            String cumulate_value = null;
            str = XmlTool.documentToJSONObject(result).toJSONString();
            // 创建JSON解析器
            JsonParser parser = new JsonParser();
            // 创建JsonObject对象
            JsonObject object = (JsonObject) parser.parse(str);
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
                logger.error("移动号码[{}]查询使用量操作出错, json-->{},xml --> {}", tel, str, result);
            }
        } catch (Exception e) {

            logger.error("移动号码[{}]解析查询使用量响应消息出错, json-->{}, xml --> {}", tel, str, result);
        }

        return null;
    }

    // 停机操作
    public void disabledNumberController(CMCCOperator operator, MsisdnMessage message) {
        boolean cutSuccess = disabledNumber(operator, message.getTel());
        if (cutSuccess == true) {
            // 停机成功 ，更新到数据库,并记录停机日志
            updateMsisdnSatus(message);
            logger.info("移动号码[{}]停机操作成功", message.getTel());
        } else  {
            // 停机失败，传到cmcc-monthly85队列
            message.setRetry(1 + message.getRetry());
            logger.info("移动号码[{}]停机操作失败，传到cmcc-stoperror", message.getTel());
            rabbitTemplate.convertAndSend("cmcc-stoperror", JSON.toJSONString(message).toString());
        }  

    }

    public boolean disabledNumber(CMCCOperator operator, String tel) {
        // 合成停机请求xml
        String xml = cmccService.tfjActionXml(tel, "1", "2", operator.getAppid(), operator.getToken(),
                operator.getGroupid());
        // 传入合成结果 得到停机返回结果
        return getDWResp(tel, xml);
    }

    // 移动停机操作
    public boolean getDWResp(String tel, String xml) {
        String result = sendRequest("停机操作", tel, xml);
        String str = null;
        try {
            str = XmlTool.documentToJSONObject(result).toJSONString();
            // 创建JSON解析器
            JsonParser parser = new JsonParser();
            // 创建JsonObject对象
            JsonObject object = (JsonObject) parser.parse(str);
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
              if(exist !=-1) {
                  logger.info("移动号码[{}]停机操作与未完成的主动停机业务冲突, xml --> {}", tel, result);
                  return true;
              }else {
                  logger.error("移动号码[{}]停机操作出错, json-->{}, xml --> {}", tel, str, result);
              }         
            }
        } catch (Exception e) {
            logger.error("移动号码[{}]解析停机操作响应消息出错,json-->{}, xml --> {}", tel, str, result);
        }

        return false;
    }
    // 记录每日流量使用量
    public void EveryDayLogWriter(MsisdnMessage message, double realApifow) {
         KuyuFlowDetail flowDetail = kuyuFlowDetailService.findLastRecord(message.getTel());
        Date now = new Date();
        long nd = 1000 * 20 * 60 * 60;// 20个小时的毫秒
        // 计算数据库最新记录和现在相隔的时间
        long divResult = (now.getTime() - (flowDetail.getTime().getTime())) - nd;

        if (divResult > 0) {
            KuyuFlowDetail kuyuFlowDetail = new KuyuFlowDetail();
            kuyuFlowDetail.setTel(message.getTel());
            kuyuFlowDetail.setCardid(message.getCardid());
            kuyuFlowDetail.setTime(new Date());
            kuyuFlowDetail.setApiflow(new BigDecimal(Double.toString(realApifow)));
            // kuyuFlowDetail.setDayflow(dayflow);
            // 切换到该套餐到现在的总使用量
            // kuyuFlowDetail.setUseflow(BigDecimal.valueOf(CumulateValue-message.getBeforeApiFlow()));
            kuyuFlowDetailService.save(kuyuFlowDetail);
                   
        }

    }

    // 停机成功 ，更新到数据库,并记录停机日志
    public void updateMsisdnSatus(MsisdnMessage message) {
        kuyuCardService.editStatus(4, message.getTel());

        KuyuCardSimstate simsate = new KuyuCardSimstate();
        simsate.setAddtime(new Date());
        simsate.setCardId(message.getCardid());
        simsate.setCard(message.getTel());
        simsate.setType(1);
        simsate.setResult(String.valueOf(0));
        simsate.setStatus(2);
        simsate.setOperatorName("监控停机");
        simsate.setRemarks("监控停机");
        kuyuCardSimstateService.save(simsate);
    }

    /**
     * 判断时间是否在时间段内
     * 
     * @param date 当前时间 yyyy-MM-dd HH:mm:ss
     * @param strDateBegin 开始时间 00:00:00
     * @param strDateEnd 结束时间 00:05:00
     * @return
     */
    public boolean isInDate(Date date, String strDateBegin, String strDateEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String strDate = sdf.format(date); // 2016-12-16 11:53:54
        // 截取当前时间时分秒 转成整型
        int tempDate = Integer
                .parseInt(strDate.substring(11, 13) + strDate.substring(14, 16) + strDate.substring(17, 19));
        // 截取开始时间时分秒 转成整型
        int tempDateBegin = Integer.parseInt(
                strDateBegin.substring(0, 2) + strDateBegin.substring(3, 5) + strDateBegin.substring(6, 8));
        // 截取结束时间时分秒 转成整型
        int tempDateEnd = Integer.parseInt(
                strDateEnd.substring(0, 2) + strDateEnd.substring(3, 5) + strDateEnd.substring(6, 8));

        if ((tempDate >= tempDateBegin && tempDate <= tempDateEnd)) {
            return true;
        } else {
            return false;
        }
    }
}
