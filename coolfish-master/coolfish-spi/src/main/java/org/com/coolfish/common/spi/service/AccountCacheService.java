package org.com.coolfish.common.spi.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.com.coolfish.common.database.entity.KuyuAccount;
import org.com.coolfish.common.database.entity.KuyuOperator;
import org.com.coolfish.common.database.service.KuyuAccountService;
import org.com.coolfish.common.database.service.KuyuOperatorService;
import org.com.coolfish.common.model.CMCCOperator;
import org.com.coolfish.common.model.CTCCOperator;
import org.com.coolfish.common.spi.cache.CMCCAccountCache;
import org.com.coolfish.common.spi.cache.CTCCAccountCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountCacheService {
    @Autowired
    private KuyuOperatorService kuyuOperatorService;

    @Autowired
    private KuyuAccountService kuyuAccountService;

    /**
     * 加载上游渠道API账号数据
     */
    // @PostConstruct
    public void load() {
        List<KuyuOperator> operatorList = kuyuOperatorService.findAccount();
        for (KuyuOperator kuyuOperator : operatorList) {
            objectTojson(0, kuyuOperator.getId(), kuyuOperator.getName(), kuyuOperator.getText());
        }

        List<KuyuAccount> accountList = kuyuAccountService.findAccount();
        for (KuyuAccount kuyuAccount : accountList) {
            objectTojson(1, kuyuAccount.getId(), kuyuAccount.getTetle(), kuyuAccount.getText());
        }

    }

    public void objectTojson(int zid, Integer id, String operator_name, String text) {
        JsonParser parser = new JsonParser();
        if (StringUtils.isNotBlank(text)) {
            JsonObject object = (JsonObject) parser.parse(text);
            try {
                // 移动供应商账号json转对象
                CMCCOperator cmccoperator = new CMCCOperator();
                cmccoperator.setAppid(object.get("appid").getAsString());
                cmccoperator.setGroupid(object.get("groupid").getAsString());
                // operator.setPassword(object.get("password").getAsString());
                cmccoperator.setToken(object.get("token").getAsString());
                cmccoperator.setOperator_name(operator_name);
                log.warn("id=[{}],移动账号的信息：{}", id, cmccoperator.toString());
                if (zid == 0) {
                    CMCCAccountCache.getInstance().putOperator(id, cmccoperator);
                    log.info("id=[{}],移动账号的信息putOperator：{}", id, cmccoperator.toString());
                } else {
                    CMCCAccountCache.getInstance().putAccount(id, cmccoperator);
                    log.info("id=[{}],移动账号的信息putAccount：{}", id, cmccoperator.toString());
                }
            } catch (Exception e) {
                try {
                    // 电信供应商账号json转对象
                    CTCCOperator ctccOperator = new CTCCOperator();
                    ctccOperator.setUsercode(object.get("usercode").getAsString());
                    ctccOperator.setPassword(object.get("password").getAsString());
                    ctccOperator.setKeys(object.get("keys").getAsString());
                    ctccOperator.setOperator_name(operator_name);
                    log.warn("id=[{}],电信账号的信息：{}", id, ctccOperator.toString());
                    if (zid == 0) {
                        CTCCAccountCache.getInstance().putOperator(id, ctccOperator);
                        log.info("id=[{}],电信账号的信息putOperator：{}--{}", id, ctccOperator.toString(),
                                CTCCAccountCache.getInstance().getOperator(id));
                    } else {
                        CTCCAccountCache.getInstance().putAccount(id, ctccOperator);
                        log.info("id=[{}],电信账号的信息putAccount：{}--{}", id, ctccOperator.toString(),
                                CTCCAccountCache.getInstance().getAccount(id));

                    }
                } catch (Exception e2) {
                    // logger.warn("非电信账号的信息：[{}]", text);
                }
            }
            log.warn("id=[{}],解析账号的信息异常,账号信息不匹配：[{}]", id, text);
        } else {
            log.warn("无账号的信息：zid=[{}],operatorid=[{}],text=[{}]", zid, id, text);
        }
    }

    public void getOneOperator(Integer id) {
        KuyuOperator operator = null;
        try {
            operator = kuyuOperatorService.getOneOperator(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != operator) {
            String text = operator.getText();
            if (StringUtils.isNotBlank(text)) {
                objectTojson(0, id, operator.getName(), text);
            }
        }
    }

    public void getOneAccount(Integer id) {
        KuyuAccount account = null;
        try {
            account = kuyuAccountService.getOneAccuount(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != account) {
            String text = account.getText();
            if (StringUtils.isNotBlank(text)) {
                objectTojson(1, id, account.getTetle(), text);
            }
        }
    }
}
