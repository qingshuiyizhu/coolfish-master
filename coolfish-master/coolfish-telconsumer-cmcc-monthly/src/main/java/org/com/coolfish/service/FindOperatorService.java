package org.com.coolfish.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.com.coolfish.entity.CMCCOperator;
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

    private Map<Long, CMCCOperator> operatorMap = null;

    private Map<Long, CMCCOperator> operatorZMap = null;

    // 直接查询移动账户信息
    public CMCCOperator getoperator(Long id) {
         if (operatorMap == null) {
            logger.info("无Zid，执行查询移动供应商账号信息");
            operatorMap = new HashMap<Long, CMCCOperator>();
            List<KuyuOperator> list = kuyuOperatorService.findAccount();
            CMCCOperator operator = null;
            JsonParser parser = new JsonParser();
            for (KuyuOperator kuyuOperator : list) {
                try {
                    operator = new CMCCOperator();
                    JsonObject object = (JsonObject) parser.parse(kuyuOperator.getText());
                    
                    operator.setAppid(object.get("appid").getAsString());
                    operator.setGroupid(object.get("groupid").getAsString());
                   // operator.setPassword(object.get("password").getAsString());
                    operator.setToken(object.get("token").getAsString());
                    
                    operatorMap.put(kuyuOperator.getId(), operator);
                    logger.info("转换成功:" + operator.toString());
                } catch (Exception e) {
                    logger.info("无Zid，类型不匹配，operatorid为："+kuyuOperator.getId() + "Text文本为："+kuyuOperator.getText());
                }
            }

        }
         logger.info("无Zid的移动供应商账号数量："+operatorMap.size());
        return operatorMap.get(id);
    }

    // 有Zid 查询移动账户信息
    public CMCCOperator getaccount(Long id) {
        if (operatorZMap == null) {
            logger.info("有Zid，执行查询供应商账号信息");
            operatorZMap = new HashMap<Long, CMCCOperator>();
            List<KuyuAccount> list = kuyuAccountService.findAccount();
            CMCCOperator operator = null;
            JsonParser parser = new JsonParser();
            for (KuyuAccount kuyuAccount : list) {
                try {
                    operator = new CMCCOperator();
                    JsonObject object = (JsonObject) parser.parse(kuyuAccount.getText());

                    operator.setAppid(object.get("appid").getAsString());
                    operator.setGroupid(object.get("groupid").getAsString());
                  //  operator.setPassword(object.get("password").getAsString());
                    operator.setToken(object.get("token").getAsString());

                    operatorZMap.put(kuyuAccount.getId(), operator);
                } catch (Exception e) {
                    logger.info("有Zid，类型不匹配，operatorid为："+kuyuAccount.getId() + "Text文本为："+kuyuAccount.getText());
                }
            }

        }
        logger.info("有Zid的供应商账号数量："+operatorZMap.size());
        return operatorZMap.get(id);
    }

}
