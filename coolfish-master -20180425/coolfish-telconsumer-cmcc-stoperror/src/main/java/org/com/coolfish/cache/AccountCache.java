package org.com.coolfish.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.com.coolfish.entity.CMCCOperator;

public class AccountCache {

    private final Map<Long, CMCCOperator> operatorMap = new ConcurrentHashMap<>();

    private final Map<Long, CMCCOperator> accountMap = new ConcurrentHashMap<>();

    public static final AccountCache INSTANCE = new AccountCache();

    private AccountCache() {

    }

    public Map<Long, CMCCOperator> getOperatorMap() {
        return operatorMap;
    }

    public Map<Long, CMCCOperator> getAccountMap() {
        return accountMap;
    }

    public static AccountCache getInstance() {
        return INSTANCE;
    }

    public CMCCOperator getOperator(Long id) {
        return operatorMap.get(id);
    }

    public CMCCOperator getAccount(Long id) {
        return accountMap.get(id);
    }

    public void putOperator(Long id, CMCCOperator operator) {
        operatorMap.put(id, operator);
    }

    public void putAccount(Long id, CMCCOperator operator) {
        accountMap.put(id, operator);
    }

}
