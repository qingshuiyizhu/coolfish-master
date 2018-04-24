package org.com.coolfish.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.com.coolfish.entity.CTCCOperator;
import org.com.coolfish.entity.KuyuAccount;
import org.com.coolfish.entity.KuyuOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class FindOperatorService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KuyuOperatorService kuyuOperatorService;

    @Autowired
    private KuyuAccountService kuyuAccountService;

    private Map<Long, CTCCOperator> operatorMap = null;

    private Map<Long, CTCCOperator> operatorZMap = null;

    // 直接查询电信账户信息
    public CTCCOperator getoperator(Long id) {

        if (null == operatorMap) {
            logger.info("无Zid，执行查询供应商账号信息");
            operatorMap = new HashMap<Long, CTCCOperator>();
            List<KuyuOperator> list = kuyuOperatorService.findAccount();
            CTCCOperator operator = null;
            JsonParser parser = new JsonParser();
            for (KuyuOperator kuyuOperator : list) {
                try {
                    operator = new CTCCOperator();
                    JsonObject object = (JsonObject) parser.parse(kuyuOperator.getText());
                    
                    operator.setUsercode(object.get("usercode").getAsString());
                    operator.setPassword(object.get("password").getAsString());
                    operator.setKeys(object.get("keys").getAsString());
                    
                    operatorMap.put(kuyuOperator.getId(), operator);
                    logger.info("转换成功:" + operator.toString());
                } catch (Exception e) {
                    logger.info("无Zid，类型不匹配:" + kuyuOperator.getText());
                }
            }
        }
        logger.info("无Zid的供应商账号数量：" + operatorMap.size());
        return operatorMap.get(id);
    }

    // 有Zid，执行查询电信供应商账号信息
    public CTCCOperator getaccount(Long id) {
        if (null == operatorZMap) {
            logger.info("有Zid，执行查询供应商账号信息");
            operatorZMap = new HashMap<Long, CTCCOperator>();
            List<KuyuAccount> list = kuyuAccountService.findAccount();
            CTCCOperator operator = null;
            JsonParser parser = new JsonParser();
            for (KuyuAccount kuyuAccount : list) {
                try {
                    operator = new CTCCOperator();
                    JsonObject object = (JsonObject) parser.parse(kuyuAccount.getText());
                   
                    operator.setUsercode(object.get("usercode").getAsString());
                    operator.setPassword(object.get("password").getAsString());
                    operator.setKeys(object.get("keys").getAsString());
                   
                    operatorZMap.put(kuyuAccount.getId(), operator);
                } catch (Exception e) {
                    logger.info("有Zid，类型不匹配:" + kuyuAccount.getText());
                }
            }
        }
        logger.info("有Zid的供应商账号数量：" + operatorZMap.size());
        return operatorZMap.get(id);
    }

}
