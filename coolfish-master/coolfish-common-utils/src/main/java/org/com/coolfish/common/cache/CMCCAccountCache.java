package org.com.coolfish.common.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.com.coolfish.common.model.CMCCOperator;

public class CMCCAccountCache {

    private final Map<Integer, CMCCOperator> operatorMap = new ConcurrentHashMap<>();

    private final Map<Integer, CMCCOperator> accountMap = new ConcurrentHashMap<>();

    public static final CMCCAccountCache INSTANCE = new CMCCAccountCache();

    private CMCCAccountCache() {

    }

    public Map<Integer, CMCCOperator> getOperatorMap() {
        return operatorMap;
    }

    public Map<Integer, CMCCOperator> getAccountMap() {
        return accountMap;
    }

    public static CMCCAccountCache getInstance() {
        return INSTANCE;
    }

    public CMCCOperator getOperator(Integer id) {
        return operatorMap.get(id);
    }

    public CMCCOperator getAccount(Integer id) {
        return accountMap.get(id);
    }

    public void putOperator(Integer id, CMCCOperator operator) {
        operatorMap.put(id, operator);
    }

    public void putAccount(Integer id, CMCCOperator operator) {
        accountMap.put(id, operator);
    }

}
