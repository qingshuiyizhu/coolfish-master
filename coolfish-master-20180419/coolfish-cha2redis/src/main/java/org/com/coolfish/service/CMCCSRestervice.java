package org.com.coolfish.service;

import java.util.List;

import org.com.coolfish.cha.utils.XmlTool;
import org.com.coolfish.config.CMCCParam;
import org.com.coolfish.entity.KuyuCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
public class CMCCSRestervice {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CMCCService cmccService;

    @Autowired
    private KuyuCardService kuyuCardService;

    @Autowired
    private CMCCParam param;

    /*
     * 3.物联网集团成员停复机
     * 
     * 请求参数 service_number ：物联网号码(取值是数字串) cycle: 查询月份 (yyyymmdd) req_seq:流水号
     */
    @Async
    public void NUllLiuLiangTFJ(Integer opid, String appid, String token, String groupid, String password)
           {
        List<KuyuCard> list = kuyuCardService.findInA(137);
        for (int i = 0; i < list.size(); i++) {
            String tel =list.get(i).getTel();
            String xml = cmccService.DKLLCXQuery(tel, 201804, "0_" + i, appid, token);
            String result = restTemplate.postForEntity(param.getUrl(), xml, String.class).getBody();
          int   leftValue = getLeftValue(result);
          if(leftValue>0) {
              logger.info("停机操作："+tel);
             xml = cmccService.TFJQuery(tel, "1", "空套餐停机", appid, token, groupid);
            result = restTemplate.postForEntity(param.getUrl(),xml,String.class).getBody();
            logger.info("停机操作："+tel+",返回结果："+result);
          }else {
              logger.info("无操作："+tel);
          }
        }

    }

    public int getLeftValue(String xml) {
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
            JsonArray prodlist = content1.get("prodlist").getAsJsonArray();
            JsonObject prodlist1 = prodlist.get(0).getAsJsonObject();

            JsonArray prodinfo = prodlist1.get("prodinfo").getAsJsonArray();
            JsonObject prodinfo1 = prodinfo.get(0).getAsJsonObject();
            left_value = prodinfo1.get("left_value").getAsInt();
        } catch (Exception e) {
            logger.error("xml->json转化失败，xml为"+xml);
            return -1;
        }
        return left_value;
    }
    }

