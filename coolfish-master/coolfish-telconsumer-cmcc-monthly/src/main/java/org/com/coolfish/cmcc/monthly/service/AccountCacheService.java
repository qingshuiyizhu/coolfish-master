package org.com.coolfish.cmcc.monthly.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.com.coolfish.common.cache.CMCCAccountCache;
import org.com.coolfish.common.database.entity.KuyuAccount;
import org.com.coolfish.common.database.entity.KuyuOperator;
import org.com.coolfish.common.database.service.KuyuAccountService;
import org.com.coolfish.common.database.service.KuyuOperatorService;
import org.com.coolfish.common.model.CMCCOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
            objectTojson(0, kuyuOperator.getId(), kuyuOperator.getText());
        }

        List<KuyuAccount> accountList = kuyuAccountService.findAccount();
        for (KuyuAccount kuyuAccount : accountList) {
            objectTojson(1, kuyuAccount.getId(), kuyuAccount.getText());
        }

    }

    public void objectTojson(int zid, Integer id, String text) {
        try {
            JsonParser parser = new JsonParser();
            CMCCOperator operator = new CMCCOperator();
            JsonObject object = (JsonObject) parser.parse(text);
            operator.setAppid(object.get("appid").getAsString());
            operator.setGroupid(object.get("groupid").getAsString());
            // operator.setPassword(object.get("password").getAsString());
            operator.setToken(object.get("token").getAsString());

            if (zid == 0) {
                CMCCAccountCache.getInstance().putOperator(id, operator);
            } else {
                CMCCAccountCache.getInstance().putAccount(id, operator);
            }
        } catch (Exception e) {
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
                objectTojson(0, id, text);
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
                objectTojson(1, id, text);
            }
        }
    }

}
