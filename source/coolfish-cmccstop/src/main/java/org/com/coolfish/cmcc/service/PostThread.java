package org.com.coolfish.cmcc.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.com.coolfish.cmcc.utils.XmlTool;
import org.com.coolfish.cmcc.vo.in.DKLLCXOperationIn;
import org.com.coolfish.cmcc.vo.in.QrystatusOperationIn;
import org.com.coolfish.cmcc.vo.in.TFJOperationIn;
import org.com.coolfish.cmcc.vo.in.content.DKLLCXContent;
import org.com.coolfish.cmcc.vo.in.content.QrystatusContent;
import org.com.coolfish.cmcc.vo.in.content.TFJContent;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.thoughtworks.xstream.XStream;

public class PostThread extends Thread {
    private final CloseableHttpClient httpClient;

    private final String tel;

    private final String url;

    private final String cycle;

    private final String req_seq;

    private final String appid;

    private final String token;

    private final String groupid;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public PostThread(CloseableHttpClient httpClient, String tel, String cycle, String req_seq, String url,
            String appid, String token, String groupid) {
        super();
        this.httpClient = httpClient;
        this.tel = tel;
        this.url = url;
        this.cycle = cycle;
        this.req_seq = req_seq;
        this.appid = appid;
        this.token = token;
        this.groupid = groupid;
    }

    public void TFJQuery(String telnum, String oprtype, String reason) throws Exception {
        HttpEntity resEntity = null;
        try {
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("Content-Type", "text/xml");
            String XML_HEADER = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n";
            String desc = "物联网集团成员停复机:" + "号码为：" + telnum + ",操作类型:" + oprtype + ",reason:操作原因:" + reason;
            logger.info(desc);
            System.out.println(desc);
            // 报文
            TFJOperationIn operationIn = new TFJOperationIn();
            // 业务功能代码
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String req_time = sdf.format(new Date());
            operationIn.setReq_time(req_time);
            operationIn.setApp_id(appid);
            operationIn.setAccess_token(token);
            // 请求类型
            operationIn.setReq_type("2");
            operationIn.setContent(new TFJContent(groupid, telnum, oprtype, reason));
            // 创建xStream对象
            XStream xStream = new XStream();
            // 设置别名, 默认会输出全路径
            xStream.alias("operation_in", TFJOperationIn.class);
            // 调用toXML 将对象转成字符串
            String s = xStream.toXML(operationIn);
            s = s.replace("__", "_");
            StringBuffer xml = new StringBuffer();
            xml.append(XML_HEADER);
            xml.append(s);
            System.out.println("停机发送xml数据为："+xml);
           logger.info("停机发送xml数据为："+xml);
            StringEntity myEntity = new StringEntity(xml.toString());
            httppost.setEntity(myEntity);
            HttpResponse response = httpClient.execute(httppost);
            resEntity = response.getEntity();
            String str = EntityUtils.toString(resEntity, Consts.UTF_8);
           // endTime = System.currentTimeMillis(); // 获取结束时间
           // logger.info(desc + "返回xml数据为:\n" + xml + "消耗时间：" + (endTime - startTime) + "ms");
            System.out.println("停机返回xml数据为："+xml);
            logger.info("停机返回xml数据为："+xml);
        } catch (Exception e) {
             logger.error("停机操作出错,号码为："+tel);
             System.out.println("停机操作出错,号码为："+tel);
        } finally {
            EntityUtils.consumeQuietly(resEntity);// 释放连接
        }
     }

    @Override
    public void run() {
        long startTime, endTime;
        startTime = System.currentTimeMillis(); // 获取开始时间
        String xml = executeAsyncDKLLCXQuery();
        String str = "";
        int left_value = -1;
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

            try {
                JsonArray prodlist = content1.get("prodlist").getAsJsonArray();
                JsonObject prodlist1 = prodlist.get(0).getAsJsonObject();

                JsonArray prodinfo = prodlist1.get("prodinfo").getAsJsonArray();
                JsonObject prodinfo1 = prodinfo.get(0).getAsJsonObject();
                left_value = prodinfo1.get("left_value").getAsInt();
            } catch (Exception e) {
                logger.error("错误：查询号码为：" + tel + "无套餐信息");
            }
            if (left_value > 0) {
                logger.info("号码为：" + tel + "，进行停机");

                TFJQuery(tel, "1", "空套餐停机");

            } else {
                logger.info("号码为：" + tel + "，无操作");
            }
        System.out.println("-------------" + left_value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        endTime = System.currentTimeMillis(); // 获取结束时间
        logger.info(tel+"停机操作完成," + "消耗时间：" + (endTime - startTime) + "ms");
    }

    private String executeAsyncQrystatusQuery() {
        long startTime, endTime;
        startTime = System.currentTimeMillis(); // 获取开始时间

        HttpEntity resEntity = null;
        try {
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("Content-Type", "text/xml");
            String XML_HEADER = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n";
            // QrystatusQuery
            String desc = "用户状态查询:" + "telnum:号码 " + tel + ",req_seq:流水号" + req_seq;
            // 报文
            QrystatusOperationIn operationIn = new QrystatusOperationIn();
            // 业务功能代码

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String req_time = sdf.format(new Date());
            operationIn.setProcess_code("cc_wlw_qrystatus");
            operationIn.setReq_time(req_time);
            operationIn.setApp_id(appid);
            operationIn.setAccess_token(token);
            // 本次调用请求流水号
            operationIn.setReq_seq(req_seq);
            // 请求类型
            operationIn.setReq_type("1");
            operationIn.setContent(new QrystatusContent(groupid, tel));
            // 创建xStream对象
            XStream xStream = new XStream();
            // 设置别名, 默认会输出全路径
            xStream.alias("operation_in", QrystatusOperationIn.class);
            // 调用toXML 将对象转成字符串
            String s = xStream.toXML(operationIn);
            s = s.replace("__", "_");
            StringBuffer xml = new StringBuffer();
            xml.append(XML_HEADER);
            xml.append(s);
            StringEntity myEntity = new StringEntity(xml.toString());
            httppost.setEntity(myEntity);
            HttpResponse response = httpClient.execute(httppost);
            resEntity = response.getEntity();
            String str = EntityUtils.toString(resEntity, Consts.UTF_8);
            endTime = System.currentTimeMillis(); // 获取结束时间
            logger.info(desc + "返回xml数据为:\n" + xml + "消耗时间：" + (endTime - startTime) + "ms");
            return req_seq + str;
        } catch (Exception e) {
        } finally {
            EntityUtils.consumeQuietly(resEntity);// 释放连接
        }
        return null;
    }

    public String executeAsyncDKLLCXQuery() {
        long startTime, endTime;
        startTime = System.currentTimeMillis(); // 获取开始时间
        HttpEntity resEntity = null;
        try {
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("Content-Type", "text/xml");
            String XML_HEADER = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n";
            // DKLLCXQuery
            String desc = "物联网单卡实时流量查询:" + "查询号码为：" + tel + ",查询月数为:" + cycle + ",req_seq:请求流水号:" + req_seq;
            // 报文
            DKLLCXOperationIn operationIn = new DKLLCXOperationIn();
            // 业务功能代码
            operationIn.setProcess_code("OPEN_QRYINTERNETUSERGPRS");
            operationIn.setContent(new DKLLCXContent(tel, cycle));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String req_time = sdf.format(new Date());
            operationIn.setReq_time(req_time);
            // 本次调用请求流水号0_9834345
            operationIn.setReq_seq(req_seq);
            operationIn.setApp_id(appid);
            operationIn.setAccess_token(token);
            // 请求类型
            operationIn.setReq_type("1");

            // 创建xStream对象
            XStream xStream = new XStream();
            // 设置别名, 默认会输出全路径
            xStream.alias("operation_in", DKLLCXOperationIn.class);
            // 调用toXML 将对象转成字符串
            String s = xStream.toXML(operationIn);
            s = s.replace("__", "_");
            StringBuffer xml = new StringBuffer();
            xml.append(XML_HEADER);
            xml.append(s);
            System.out.println(xml.toString());
            StringEntity myEntity = new StringEntity(xml.toString());
            httppost.setEntity(myEntity);
            HttpResponse response = httpClient.execute(httppost);
            resEntity = response.getEntity();
            String str = EntityUtils.toString(resEntity, Consts.UTF_8);
            endTime = System.currentTimeMillis(); // 获取结束时间
            // logger.info(desc + "返回xml数据为:\n" + xml+"消耗时间：" + (endTime - startTime) +
            // "ms");
            return str;
        } catch (Exception e) {
        } finally {
            EntityUtils.consumeQuietly(resEntity);// 释放连接
        }
        return null;
    }
}
