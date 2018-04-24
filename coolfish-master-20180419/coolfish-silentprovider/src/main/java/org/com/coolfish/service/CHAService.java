package org.com.coolfish.service;

import org.com.coolfish.cha.utils.DesUtils;
import org.com.coolfish.config.CHAParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

/***
 * 
 * @author LINGHUI
 * @desc 中国电信的http get 请求数据 封装
 *
 */
@Service
public class CHAService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CHAParam chaParam;
 
    @Async
    public void queryCardStatus(String access_number, String monthDate,int i) {
        String method = "queryTraffic";
        // keys = 23n3AHxkQ
        String user_id = chaParam.getUser_id();
        String key1 = chaParam.getkey1();
        String key2 = chaParam.getkey2();
        String key3 = chaParam.getkey3();
        
        String[] arr = {access_number, user_id, chaParam.getPassword(), method };
        DesUtils des = new DesUtils();
        String passWord = des.strEnc(chaParam.getPassword(), key1, key2, key3);
        String sign = des.strEnc(DesUtils.naturalOrdering(arr), key1, key2, key3);
        String url = chaParam.getUrl()+"?method=" + method + "&user_id=" + user_id
                + "&access_number=" + access_number + "&passWord=" + passWord + "&sign=" + sign;
       String result = restTemplate.getForEntity(url, String.class).getBody();
        System.out.println(i+"result:" + result+key1+"--"+key2+"---"+key3+"--"+user_id+"=="+chaParam.getPassword());
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
