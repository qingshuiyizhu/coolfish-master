package org.com.coolfish.common.webinterface.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.com.coolfish.common.model.CTCCOperator;

public class CTCCAccountCache {

    private final Map<Integer, CTCCOperator> operatorMap = new ConcurrentHashMap<>();

    private final Map<Integer, CTCCOperator> accountMap = new ConcurrentHashMap<>();

    public static final CTCCAccountCache CTCCAccountINSTANCE = new CTCCAccountCache();

    private CTCCAccountCache() {

    }

    public Map<Integer, CTCCOperator> getOperatorMap() {
        return operatorMap;
    }

    public Map<Integer, CTCCOperator> getAccountMap() {
        return accountMap;
    }

    public static CTCCAccountCache getInstance() {
        return CTCCAccountINSTANCE;
    }

    public CTCCOperator getOperator(Integer id) {
        return operatorMap.get(id);
    }

    public CTCCOperator getAccount(Integer id) {
        return accountMap.get(id);
    }

    public void putOperator(Integer id, CTCCOperator operator) {
        operatorMap.put(id, operator);
    }

    public void putAccount(Integer id, CTCCOperator operator) {
        accountMap.put(id, operator);
    }

}
