package org.com.coolfish.common.webinterface.service;

import org.apache.commons.lang.StringUtils;
import org.com.coolfish.common.model.CTCCOperator;
import org.com.coolfish.common.model.DisabledBean;
import org.com.coolfish.common.model.UtilBean;
import org.com.coolfish.common.util.DecimalTools;
import org.com.coolfish.common.util.DesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.extern.slf4j.Slf4j;

/***
 * 
 * @author LINGHUI
 * @desc 中国电信
 *
 */
@Slf4j
@Service
public class CTCCResquestService {
    private final static String CTCCURL = "http://api.ct10649.com:9001/m2m_ec/query.do";

    @Autowired
    private RequestService requestService;

    private JsonParser parser = new JsonParser();

    // 查询号码状态
    public UtilBean queryStatus(CTCCOperator operator, UtilBean requestBean) {
        String accessNumber = requestBean.getIphone();
        String userId = operator.getUsercode();
        String password = operator.getPassword();
        String method = "queryCardStatus";

        String key1 = operator.getkey1();
        String key2 = operator.getkey2();
        String key3 = operator.getkey3();
        String iccid = requestBean.getIccid();

        String url = assembleUrl(userId, password, key1, key2, key3, method, accessNumber, iccid);
        String[] arr = { accessNumber, userId, password, method };
        String sign = sign(arr, key1, key2, key3);
        url = url + "&sign=" + sign;
        String result = requestService.sendRequest("电信号码状态查询", requestBean.getCardId(), url, null);

        return analyzeQueryStauts(requestBean, result);
    }

    public UtilBean analyzeQueryStauts(UtilBean requestBean, String result) {
        try {

            JsonObject object = (JsonObject) parser.parse(result);
            JsonArray responseJsonArray = object.get("Query_response").getAsJsonArray();
            JsonObject responseJsonObject = responseJsonArray.get(0).getAsJsonObject();

            JsonArray prodRecordsJsonArray = responseJsonObject.get("prodRecords").getAsJsonArray();
            JsonObject prodRecordsJsonObject = prodRecordsJsonArray.get(0).getAsJsonObject();

            JsonArray prodRecordJsonArray = prodRecordsJsonObject.get("prodRecord").getAsJsonArray();
            JsonObject prodRecordJsonObject = prodRecordJsonArray.get(0).getAsJsonObject();

            JsonArray productInfoJsonArray = prodRecordJsonObject.get("productInfo").getAsJsonArray();
            JsonObject productInfoJsonObject = productInfoJsonArray.get(0).getAsJsonObject();
            String resultCode = productInfoJsonObject.get("productStatusCd").getAsString();
            String resultMsg = productInfoJsonObject.get("productStatusName").getAsString();
            // 返回状态码
            requestBean.setResultCode(resultCode);
            requestBean.setResultMsg(resultMsg);
        } catch (Exception e) {
            log.error("电信物联网卡ID[{}]解析查询号码状态响应消息出错,json-->{}", requestBean.getCardId(), result);
            requestBean.setResultCode("-1");
            requestBean.setResultMsg("解析第三响应消息发生异常");
        }
        return requestBean;
    }

    // 查询当月的使用流量
    public UtilBean queryTraffic(CTCCOperator operator, UtilBean requestBean) {
        String method = "queryTraffic";// 流量查询当月接口
        String userId = operator.getUsercode();
        String password = operator.getPassword();
        String key1 = operator.getkey1();
        String key2 = operator.getkey2();
        String key3 = operator.getkey3();
        String access_number = requestBean.getIphone();
        String iccid = requestBean.getIccid();

        String[] arr = { access_number, userId, operator.getPassword(), method };
        String url = assembleUrl(userId, password, key1, key2, key3, method, access_number, iccid);
        String sign = sign(arr, key1, key2, key3);
        url = url + "&sign=" + sign + "&Dt1=0";
        return getCumulateValue(url, requestBean);

    }

    // 得到当月使用流量->提取数据
    public UtilBean getCumulateValue(String url, UtilBean requestBean) {
        // 调用接口
        String result = requestService.sendRequest("电信号码使用量查询", requestBean.getCardId(), url, null);
        try {
            JsonObject object = (JsonObject) parser.parse(result);
            JsonArray content = object.get("NEW_DATA_TICKET_QRsp").getAsJsonArray();
            JsonObject content1 = content.get(0).getAsJsonObject();
            String value = content1.get("TOTAL_BYTES_CNT").getAsString();
            String status = content1.get("IRESULT").getAsString();
             if ("0".equals(status)) {
                // 请求数据成功
                String cumulateValue = value.substring(0, value.length() - 2);
                // 得到的数据是MB 转换为KB
                requestBean.setAnalyze(DecimalTools.mul(cumulateValue, "1024"));
            } else {
                log.error("电信物联网卡ID[{}]查询月套餐使用量出错,json-->{}", requestBean.getCardId(), result);
                requestBean.setResultCode("-1");
                requestBean.setResultMsg("查询使用量异常");
            }
        } catch (Exception e) {
             log.error("电信物联网卡ID[{}]解析月套餐使用量响应消息出错,json-->{}", requestBean.getCardId(), result);
             requestBean.setResultCode("-1");
             requestBean.setResultMsg("解析第三响应消息发生异常");
        }
        return requestBean;
    }

    // 停复机操作
    public DisabledBean disabledNumber(CTCCOperator operator, DisabledBean requestBean) {
        String userId = operator.getUsercode();
        String password = operator.getPassword();
        String key1 = operator.getkey1();
        String key2 = operator.getkey2();
        String key3 = operator.getkey3();
        String method = "disabledNumber";// 停机接口
        String access_number = requestBean.getIphone();
        String iccid = requestBean.getIccid();

        String orderTypeId = ""; // 停机保号类,19表示停机，20表示复机
        if ("2".equals(requestBean.getOprtype())) {
            // 复机
            orderTypeId = "20";
        } else if ("1".equals(requestBean.getOprtype())) {
            // 停机
            orderTypeId = "19";
        }

        String[] arr = { access_number, userId, operator.getPassword(), method, "", orderTypeId };
        String url = assembleUrl(userId, password, key1, key2, key3, method, access_number, iccid);
        String sign = sign(arr, key1, key2, key3);
        url = url + "&orderTypeId=" + orderTypeId + "&acctCd=" + "&sign=" + sign;
        return analyzeDisabledNumber(requestBean, url);
    }

    // 电信停复机解析
    public DisabledBean analyzeDisabledNumber(DisabledBean disabledBean, String url) {
        String action = "电信号码停复机操作类型-未知";
        if ("1".equals(disabledBean.getOprtype())) {
            action = "电信号码停复机操作类型-停机";
        } else if ("2".equals(disabledBean.getOprtype())) {
            action = "电信号码停复机操作类型-复机";
        }

        String result = requestService.sendRequest(action, disabledBean.getCardId(), url, null);
        try {
            JsonObject object = (JsonObject) parser.parse(result);
            // 得到为json的数组
            String resultCode = object.get("result").getAsString();

            String resultMsg = object.get("resultMsg").getAsString();
            String serialNumber = object.get("GROUP_TRANSACTIONID").getAsString();

            disabledBean.setResultMsg(resultMsg);
            disabledBean.setResultCode(resultCode);
            disabledBean.setSerialNumber(serialNumber);
        } catch (Exception e) {
            log.error("电信物联网卡ID[{}]解析复机操作响应消息出错,json-->{}", disabledBean.getCardId(), result);
            disabledBean.setResultCode("-1");
            disabledBean.setResultMsg("解析第三响应消息发生异常");
        }
        return disabledBean;

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
        sb.append(CTCCURL);
        sb.append("?method=").append(method);
        if (StringUtils.isNotBlank(accessNumber)) {
            sb.append("&access_number=").append(accessNumber);
        } else {
            sb.append("&iccid=").append(iccid);

        }
        sb.append("&user_id=").append(userId);
        String passwordEnc = DesUtils.strEnc(password, key1, key2, key3); // 密码加密
        sb.append("&passWord=").append(passwordEnc);
        return sb.toString();
    }

}
