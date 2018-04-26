package org.com.coolfish.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.com.coolfish.entity.CTCCOperator;

public class AccountCache {

    private final Map<Long, CTCCOperator> operatorMap = new ConcurrentHashMap<>();

    private final Map<Long, CTCCOperator> accountMap = new ConcurrentHashMap<>();
    
    public static final AccountCache INSTANCE = new AccountCache();
    
    private AccountCache() {
        
    }
    
    public Map<Long, CTCCOperator> getOperatorMap() {
        return operatorMap;
    }

    public Map<Long, CTCCOperator> getAccountMap() {
        return accountMap;
    }


    public static AccountCache getInstance() {
        return INSTANCE;
    }
    
    public CTCCOperator getOperator(Long id) {
        return operatorMap.get(id);
    }
    public CTCCOperator getAccount(Long id) {
        return accountMap.get(id);
    }
    
    public void putOperator(Long id, CTCCOperator operator) {
        operatorMap.put(id, operator);
    }
    
    public void putAccount(Long id, CTCCOperator operator) {
        accountMap.put(id, operator);
    }
    
   
}

