package org.com.coolfish.common.webinterface.service;

import org.com.coolfish.common.util.XmlTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class RequestService {
    @Autowired
    private RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 调用供应商接口，返回json数据
    public String sendRequest(String interfaceName, String tel, String url, String parameter) {
        logger.info("号码[{}]调用[{}]接口,请求路径[{}],请求参数[{}]", tel, interfaceName, url, parameter);
        long startTime = System.currentTimeMillis();
        String result = null;
        try {
            if (parameter != null) {
                result = restTemplate.postForEntity(url, parameter, String.class).getBody();
            } else {
                result = restTemplate.getForEntity(url, String.class).getBody();
            }
            logger.info("号码[{}]调用[{}]接口,耗时:{}ms,响应消息[{}] ", tel, interfaceName,
                    (System.currentTimeMillis() - startTime), result);
        } catch (Exception e) {
            logger.error("号码[{}]调用[{}]接口发生异常, 耗时:{}ms,响应消息[{}]", tel, interfaceName,
                    (System.currentTimeMillis() - startTime), result);
        }
        String jsonResult = null;
        try {
            jsonResult = XmlTool.documentToJSONObject(result).toJSONString();
            logger.info("号码[{}]调用[{}]接口响应消息转换的JSON[{}]", tel, interfaceName,jsonResult);
        } catch (Exception e) {
            logger.error("号码[{}]调用[{}]接口响应消息转换JSON失败,响应的消息[{}]", tel, interfaceName,result);
        }

        return jsonResult;
    }

}
