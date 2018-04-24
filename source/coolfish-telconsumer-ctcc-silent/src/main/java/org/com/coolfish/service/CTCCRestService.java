package org.com.coolfish.service;

import java.util.Date;
import java.util.List;

import org.com.coolfish.config.CHAParam;
import org.com.coolfish.ctcc.utils.DesUtils;
import org.com.coolfish.ctcc.utils.XmlTool;
import org.com.coolfish.entity.ChaOperator;
import org.com.coolfish.entity.KuyuAddPackage;
import org.com.coolfish.entity.MqJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
    private CHAParam param;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private KuyuCardService kuyuCardService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private KuyuAddPackageService kuyuAddPackageService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 电信处理沉默期
    public void handSlient(ChaOperator operator, MqJson json) {
        long startTime, endTime;
        startTime = System.currentTimeMillis(); // 获取开始时间
        // 得到使用量
        double value = -1;
        for (int i = 0; i < 3; i++) {
            value = queryTraffic(operator, json.getTel(), null);
            if (value != -1) {
                // 请求成功
                logger.info("电信号码流量获取成功，当月使用量为：" + value);
                break;
            }
        }
        if (value == -1) {
            logger.info("电信号码流量获取失败，扔回队列cha-silent" + json.getTel());
            // 流程查询失败，扔回队列
            rabbitTemplate.convertAndSend("cha-silent", JSON.toJSONString(json) + new Date());
        }
        // 处理沉默期
        if (value > 0) {
            logger.info("电信号码处理沉默期:" + json.getTel());
            // kuyu_card 表里的statusetime = now()
            kuyuCardService.upTime(json.getTel());
            List<KuyuAddPackage> addPackages = kuyuAddPackageService.findAllSlient(json.getTel());
            KuyuAddPackage addpackage = null;
            for (int i = 0; i < addPackages.size(); i++) {
                // 添加时间
                addpackage = addPackages.get(i);
                long addEndTime = addpackage.getEndtime().getTime()
                        + (new Date().getTime() - addpackage.getStarttime().getTime());
                // 结束时间
                Date ed = new Date(addEndTime);
                kuyuAddPackageService.upPackageTime(new Date(), ed, addpackage.getId());

            }
        }else {
            rabbitTemplate.convertAndSend("cha-silent", JSON.toJSONString(json) + new Date());
        }
        endTime = System.currentTimeMillis(); // 获取结束时间
        logger.info("电信号码处理沉默期：" + json.getTel() + "消耗时间：" + (endTime - startTime) + "ms");
    }

    public int singCutNet(ChaOperator operator, String access_number) {
        String method = "singCutNet";// 单独断网接口
        String user_id = operator.getUsercode();
        String action = "ADD";// 单独断网
        String key1 = operator.getkey1();
        String key2 = operator.getkey2();
        String key3 = operator.getkey3();
        String[] arr = { access_number, user_id, operator.getPassword(), method };
        DesUtils des = new DesUtils();
        String passWord = des.strEnc(operator.getPassword(), key1, key2, key3);
        String sign = des.strEnc(DesUtils.naturalOrdering(arr), key1, key2, key3);
        String url = param.getUrl() + "?method=" + method + "&user_id=" + user_id + "&access_number="
                + access_number + "&action=" + action + "&passWord=" + passWord + "&sign=" + sign;
        // 得到结果
        String result = restTemplate.getForEntity(url, String.class).getBody();
        // 解析xml 得到断网返回结果
        return getDWResp(result);

    }

    public int getDWResp(String xml) {
        String str = "";
        try {
            str = XmlTool.documentToJSONObject(xml).toJSONString();
            System.out.println(str);
            // 创建JSON解析器
            JsonParser parser = new JsonParser();
            // 创建JsonObject对象
            JsonObject object = (JsonObject) parser.parse(str);
            // 得到为json的数组
            JsonArray content = object.get("SvcCont").getAsJsonArray();
            JsonObject content1 = content.get(0).getAsJsonObject();
            JsonArray response = content1.get("Response").getAsJsonArray();
            JsonObject response1 = response.get(0).getAsJsonObject();
            String value = response1.get("ResCode").getAsString();
            if ("0000".equals(value)) {
                // 单独断网成功
                return 0;
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    // 查询当月的使用流量
    public double queryTraffic(ChaOperator operator, String access_number, String monthDate) {
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
        // 得到结果
        String result = restTemplate.getForEntity(url, String.class).getBody();
        // 解析，当月得到使用量
        return getCumulateValue(result);
    }

    // 得到当月使用流量->提取数据
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
            JsonArray content = object.get("NEW_DATA_TICKET_QRsp").getAsJsonArray();
            JsonObject content1 = content.get(0).getAsJsonObject();
            String value = content1.get("TOTAL_BYTES_CNT").getAsString();
            String status = content1.get("IRESULT").getAsString();
            cumulate_value = Double.parseDouble(value.substring(0, value.length() - 2));
            // 得到的数据是MB 转换为KB
            cumulate_value = 1024 * cumulate_value;
            System.out.println("========" + cumulate_value + "-----" + status);
            if ("0".equals(status)) {
                // 请求数据成功
                return cumulate_value;
            }
        } catch (Exception e) {// ========16504.45MB-----0
            return -1;
        }
        return -1;

    }

    public Object genJson() {
        JSONObject json = new JSONObject();
        json.put("descp", "this is spring rest template sample");
        return json;
    }

    /********** HTTP POST method **************/

    public Object iAmPostApi(@RequestBody JSONObject parm) {
        System.out.println(parm.toJSONString());
        parm.put("result", "hello post");
        return parm;
    }

    public Object testPost() {
        String url = "http://localhost:8080/postApi";
        JSONObject postData = new JSONObject();
        postData.put("descp", "request for post");
        JSONObject json = restTemplate.postForEntity(url, postData, JSONObject.class).getBody();
        return json.toJSONString();
    }

}
