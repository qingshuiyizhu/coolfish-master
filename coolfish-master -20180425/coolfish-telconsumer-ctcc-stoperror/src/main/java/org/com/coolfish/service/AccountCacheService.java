package org.com.coolfish.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.com.coolfish.cache.AccountCache;
import org.com.coolfish.entity.CTCCOperator;
import org.com.coolfish.entity.KuyuAccount;
import org.com.coolfish.entity.KuyuOperator;
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

    public void objectTojson(int zid, Long id, String text) {
        try {
            JsonParser parser = new JsonParser();
            CTCCOperator operator = new CTCCOperator();
            JsonObject object = (JsonObject) parser.parse(text);
            operator.setUsercode(object.get("usercode").getAsString());
            operator.setPassword(object.get("password").getAsString());
            operator.setKeys(object.get("keys").getAsString());
            if (zid == 0) {
                AccountCache.getInstance().putOperator(id, operator);
            } else {
                AccountCache.getInstance().putAccount(id, operator);
            }
        } catch (Exception e) {
        }
    }

    public void getOneOperator(Long id) {
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

    public void getOneAccount(Long id) {
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
