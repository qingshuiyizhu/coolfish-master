package org.com.coolfish.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.com.coolfish.config.CTCCParam;
import org.com.coolfish.entity.CTCCOperator;
import org.com.coolfish.entity.KuyuCardSimstate;
import org.com.coolfish.entity.KuyuFlowDetail;
import org.com.coolfish.message.MsisdnMessage;
import org.com.coolfish.util.DecimalTools;
import org.com.coolfish.util.DesUtils;
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

/***
 * 
 * @author LINGHUI
 * @desc 中国电信的http get 请求API
 *
 */
@Service
public class CTCCRestService {
    @Autowired
    private CTCCParam param;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private KuyuCardService kuyuCardService;

    @Autowired
    private KuyuCardSimstateService kuyuCardSimstateService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private KuyuFlowDetailService kuyuFlowDetailService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 
     * @param interfaceName
     * @param tel
     * @param url
     * @return
     * 
     *         调用电信API接口
     * 
     * 
     */
    public String sendRequest(String interfaceName, String tel, String url) {
        logger.info("电信号码[{}]调用API接口[{}]请求消息, url--> {}", tel, interfaceName, url);
        long startTime = System.currentTimeMillis();
        String result = null;
        try {
            result = restTemplate.getForEntity(url, String.class).getBody();
            logger.info("电信号码[{}]调用API接口[{}]响应消息, 耗时:{}, xml--> {}", tel, interfaceName,
                    (System.currentTimeMillis() - startTime), result);
        } catch (Exception e) {
            logger.error("电信号码[{}]调用API接口[{}]发生异常错误, 耗时:{}, xml--> {}", tel, interfaceName,
                    (System.currentTimeMillis() - startTime), result);
        }

        return result;
    }

    // 电信月流量监控
    public void handle(CTCCOperator operator, MsisdnMessage message) {
        // 查询电信当月使用量
        String nowApifow = queryTraffic(operator, message.getTel());
        double realApiflow = 0;
        if (nowApifow == null) {
            // 流程查询失败，传到队列ctcc-monthly85
            logger.info("电信号码[{}]本月使用量获取失败，传到队列ctcc-monthly85", message.getTel());
            rabbitTemplate.convertAndSend("ctcc-monthly85", JSON.toJSONString(message).toString());
        } else {
            realApiflow = DecimalTools.div(nowApifow, message.getPer(), 2);

            // 总流量-使用量<=0 判断是否停机
            double subResult = DecimalTools.sub(message.getSumflow(), Double.toString(realApiflow));
            // 使用量/总流量 判断是否重点监控
            double result85 = DecimalTools.div(Double.toString(realApiflow), message.getSumflow(), 2);

            java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            logger.info("电信号码[{}]的套餐总量[{} kb], 已使用量[{} kb], 使用率[{}%]", message.getTel(), message.getSumflow(),
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
                        logger.info("移动号码[{}]状态:[{}], 忽略停机操作", message.getTel(), status);
                    }
                }

                // 停机操作
                logger.info("电信号码停机操作：" + message.getTel());
                disabledNumberController(operator, message);
            } else if (DecimalTools.compareTo(result85, 0.85d) > -1) {
                // 传到电信月套餐使用率达85的队列
                // #更新使用量
                message.setUseflow(Double.toString(realApiflow));
                rabbitTemplate.convertAndSend("ctcc-monthly85", JSON.toJSONString(message).toString());
                logger.info("电信号码流量使用率达到85%，传到ctcc-monthly85：" + JSON.toJSONString(message).toString());
            } else {
                logger.info("电信号码流量使用量正常，不进行任何操作：" + message.getTel());
            }
        }

        // 进行每日流量使用量记录写入数据库
        boolean isInsert = isInDate(new Date(), param.getLogstartTime(), param.getLogendTime());
        if (isInsert) {
            EveryDayLogWriter(message, realApiflow);
        }
    }

    // 查询号码状态
    private String queryStauts(CTCCOperator operator, MsisdnMessage message) {
        String accessNumber = message.getTel();
        String userId = operator.getUsercode();
        String password = operator.getPassword();
        String method = "queryCardStatus";

        String key1 = operator.getkey1();
        String key2 = operator.getkey2();
        String key3 = operator.getkey3();

        String url = assembleUrl(userId, password, key1, key2, key3, method, accessNumber, null);
        String[] arr = { accessNumber, userId, password, method };
        String sign = sign(arr, key1, key2, key3);
        url = url + "&sign=" + sign;
        return queryStautsRequest(message.getTel(), url);
    }

    private String queryStautsRequest(String tel, String url) {
        String result = sendRequest("查询号码状态", tel, url);
        try {
            String str = XmlTool.documentToJSONObject(result).toJSONString();
            System.out.println(str);
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(str);
            JsonArray responseJsonArray = object.get("Query_response").getAsJsonArray();
            JsonObject responseJsonObject = responseJsonArray.get(0).getAsJsonObject();

            JsonArray prodRecordsJsonArray = responseJsonObject.get("prodRecords").getAsJsonArray();
            JsonObject prodRecordsJsonObject = prodRecordsJsonArray.get(0).getAsJsonObject();

            JsonArray prodRecordJsonArray = prodRecordsJsonObject.get("prodRecord").getAsJsonArray();
            JsonObject prodRecordJsonObject = prodRecordJsonArray.get(0).getAsJsonObject();

            JsonArray productInfoJsonArray = prodRecordJsonObject.get("productInfo").getAsJsonArray();
            JsonObject productInfoJsonObject = productInfoJsonArray.get(0).getAsJsonObject();
            String status = productInfoJsonObject.get("productStatusCd").getAsString();

            if ("1".equals(status)) {
                // 状态在用
                return "1";
            } else {
                return productInfoJsonObject.get("productStatusName").getAsString();
            }
        } catch (Exception e) {
            logger.error("电信号码[{}]解析使用量查询响应消息出错,xml--> {}", tel, result);
        }
        return null;
    }

    // 查询当月的使用流量
    @SuppressWarnings("static-access")
    public String queryTraffic(CTCCOperator operator, String access_number) {
        String method = "queryTraffic";// 流量查询当月接口
        String user_id = operator.getUsercode();
        String key1 = operator.getkey1();
        String key2 = operator.getkey2();
        String key3 = operator.getkey3();
        String[] arr = { access_number, user_id, operator.getPassword(), method };
        DesUtils des = new DesUtils();
        String passWord = des.strEnc(operator.getPassword(), key1, key2, key3);
        String sign = des.strEnc(DesUtils.naturalOrdering(arr), key1, key2, key3);
        String url = param.getUrl() + "?method=" + method + "&user_id=" + user_id + "&access_number="
                + access_number + "&passWord=" + passWord + "&sign=" + sign + "&Dt1=0";

        return getCumulateValue(url, access_number);

    }

    // 得到当月使用流量->提取数据
    public String getCumulateValue(String url, String tel) {
        // 调用接口
        String result = sendRequest("查询月套餐使用量", tel, url);
        try {

            String str = XmlTool.documentToJSONObject(result).toJSONString();
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(str);
            JsonArray content = object.get("NEW_DATA_TICKET_QRsp").getAsJsonArray();
            JsonObject content1 = content.get(0).getAsJsonObject();
            String value = content1.get("TOTAL_BYTES_CNT").getAsString();
            String status = content1.get("IRESULT").getAsString();
            if ("0".equals(status)) {
                // 请求数据成功
                String cumulateValue = value.substring(0, value.length() - 2);
                // 得到的数据是MB 转换为KB
                return DecimalTools.mul(cumulateValue, "1024");
            } else {
                logger.error("电信号码[{}]使用量查询操作出错,xml --> {}", tel, result);
            }
        } catch (Exception e) {
            logger.error("电信号码[{}]解析使用量查询响应消息出错,xml--> {}", tel, result);
        }
        return null;
    }

    // 电信停机处理方法
    public void disabledNumberController(CTCCOperator operator, MsisdnMessage message) {
        boolean cutSuccess = disabledNumber(operator, message.getTel());
        if (cutSuccess == true) {
            // 停机成功 ，更新到数据库,并记录停机日志
            updateMsisdnSatus(message);
            logger.info("电信号码[{}]停机成功", message.getTel());
        } else if (message.getRetry() < param.getRemax()) {
            // 停机失败， 总重试次数+1丢回队列
            message.setRetry(1 + message.getRetry());
            logger.info("电信号码[{}]停机失败，传到ctcc-monthly85队列", message.getTel());
            rabbitTemplate.convertAndSend("ctcc-monthly85", JSON.toJSONString(message).toString());
        } else {
            // 重试次数达到最大，传到email队列
            logger.info("电信号码[{}]停机失败重试次数达到最大，传到email队列" + message.getTel());
            rabbitTemplate.convertAndSend("email", message.getTel());
        }

    }

    // 停机操作
    public boolean disabledNumber(CTCCOperator operator, String access_number) {
        String userId = operator.getUsercode();
        String password = operator.getPassword();
        String key1 = operator.getkey1();
        String key2 = operator.getkey2();
        String key3 = operator.getkey3();
        String method = "disabledNumber";// 停机接口

        String url = assembleUrl(userId, password, key1, key2, key3, method, access_number, null);

        String[] arr = { access_number, userId, operator.getPassword(), method, "20", "" };
        String sign = sign(arr, key1, key2, key3);

        url = url + "&orderTypeId=19" + "&acctCd=" + "&sign=" + sign;

        return getDWResp(url, access_number);

    }

    // 解析电信停机返回参数
    public boolean getDWResp(String url, String tel) {
        String result = sendRequest("停机操作", tel, url);
        try {
            String str = XmlTool.documentToJSONObject(result).toJSONString();
            // 创建JSON解析器
            JsonParser parser = new JsonParser();
            // 创建JsonObject对象
            JsonObject object = (JsonObject) parser.parse(str);
            // 得到为json的数组
            JsonArray response = object.get("businessServiceResponse").getAsJsonArray();
            JsonObject businessServiceResponse = response.get(0).getAsJsonObject();
            String rspType = businessServiceResponse.get("RspType").getAsString();
            if ("0".equals(rspType)) {
                logger.info("电信号码[{}]停机成功, xml --> {}", tel, result);
                return true;
            } else {
                logger.error("移动号码[{}]停机操作出错, xml --> {}", tel, result);
            }
        } catch (Exception e) {
            logger.error("移动号码[{}]解析停机响应消息出错, xml --> {}", tel, result);

        }
        return false;
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
        simsate.setOperatorName("流量监控停机");
        kuyuCardSimstateService.save(simsate);

    }

    // 进行每日流量使用量记录写入数据库
    public void EveryDayLogWriter(MsisdnMessage message, double realApifow) {

        Date time = kuyuFlowDetailService.findMaxSaveTime(message.getTel());
        Date now = new Date();
        long nd = 1000 * 20 * 60 * 60;// 一天的毫秒数
        // 计算数据库最新记录和现在相隔的时间
        long divResult = now.getTime() - time.getTime() - nd;

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

    private String sign(String[] arr, String key1, String key2, String key3) {
        String val = DesUtils.naturalOrdering(arr);
        return DesUtils.strEnc(val, key1, key2, key3);
    }

    /**
     * 组装公共部分的url地址
     * 
     * @param accessNumber
     * @param method
     * @param iccid
     * @return
     */
    private String assembleUrl(String userId, String password, String key1, String key2, String key3,
            String method, String accessNumber, String iccid) {
        StringBuffer sb = new StringBuffer();
        sb.append(param.getUrl());
        sb.append("?method=").append(method);
        if (StringUtils.isNotBlank(iccid)) {
            sb.append("&iccid=").append(iccid);
        } else {
            sb.append("&access_number=").append(accessNumber);
        }
        sb.append("&user_id=").append(userId);
        String passwordEnc = DesUtils.strEnc(password, key1, key2, key3); // 密码加密
        sb.append("&passWord=").append(passwordEnc);
        return sb.toString();
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
