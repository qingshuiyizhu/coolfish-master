package org.com.coolfish.common.webinterface.service;

import org.apache.commons.lang.StringUtils;
import org.com.coolfish.common.message.MsisdnMessage;
import org.com.coolfish.common.model.CTCCOperator;
import org.com.coolfish.common.util.DecimalTools;
import org.com.coolfish.common.util.DesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/***
 * 
 * @author LINGHUI
 * @desc 中国电信
 *
 */
@Service
public class CTCCResquestService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static String CTCCURL = "http://api.ct10649.com:9001/m2m_ec/query.do";

    @Autowired
    private RequestService requestService;

    private JsonParser parser = new JsonParser();

    // 查询号码状态
    public String queryStauts(CTCCOperator operator, MsisdnMessage message) {
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

    public String queryStautsRequest(String tel, String url) {
        String result = requestService.sendRequest("电信号码状态查询", tel, url, null);
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
            String status = productInfoJsonObject.get("productStatusCd").getAsString();

            if ("1".equals(status)) {
                // 状态在用
                return "1";
            } else {
                return productInfoJsonObject.get("productStatusName").getAsString();
            }
        } catch (Exception e) {
            logger.error("电信号码[{}]解析查询号码状态响应消息出错,json-->{}", tel, result);
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
        String url = CTCCURL + "?method=" + method + "&user_id=" + user_id + "&access_number=" + access_number
                + "&passWord=" + passWord + "&sign=" + sign + "&Dt1=0";

        return getCumulateValue(url, access_number);

    }

    // 得到当月使用流量->提取数据
    public String getCumulateValue(String url, String tel) {
        // 调用接口
        String result = requestService.sendRequest("电信号码使用量查询", tel, url, null);
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
                return DecimalTools.mul(cumulateValue, "1024");
            } else {
                logger.error("电信号码[{}]查询月套餐使用量出错,json-->{}", result);
            }
        } catch (Exception e) {
            logger.error("电信号码[{}]解析月套餐使用量响应消息出错,json-->{}", tel,result);
        }
        return null;
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

        String[] arr = { access_number, userId, operator.getPassword(), method, "19", "" };
        String sign = sign(arr, key1, key2, key3);

        url = url + "&orderTypeId=19" + "&acctCd=" + "&sign=" + sign;

        return getDWResp(url, access_number);

    }

    // 解析电信停机返回参数
    public boolean getDWResp(String url, String tel) {
        String result = requestService.sendRequest("电信号码停机操作", tel, url, null);
         try {
              JsonObject object = (JsonObject) parser.parse(result);
            // 得到为json的数组
            String rspType = object.get("RspType").getAsString();
            if ("0".equals(rspType)) {
                logger.info("电信号码[{}]停机操作成功, json-->{}", tel,result);
                return true;
            } else if ("1".equals(rspType)) {
                String stop = object.get("result").getAsString();
                if ("1999".equals(stop)) {
                    logger.error("电信号码[{}]|已存在停机类型为120000的停机记录，无法进行停机操作,json-->{}", tel,result);
                    return true;
                }
            } else {
                logger.error("电信号码[{}]停机操作出错,json-->{}", tel, result);
            }
        } catch (Exception e) {
            logger.error("电信号码[{}]解析停机操作响应消息出错, json-->{}", tel,result);

        }
        return false;
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

}
