package org.com.coolfish.common.spi.service;

import org.com.coolfish.common.model.CMCCOperator;
import org.com.coolfish.common.model.CTCCOperator;
import org.com.coolfish.common.webinterface.cache.CMCCAccountCache;
import org.com.coolfish.common.webinterface.cache.CTCCAccountCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class GetKeyService {

    // 不关联账户密钥表
    private final static int NOZID = 0;

    @Autowired
    private AccountCacheService accountCacheService;
    
    
    public CMCCOperator getCMCCOperator(Integer operatorid, Integer zid) {
        CMCCOperator operator = null;
        if (NOZID == zid) {
            operator = CMCCAccountCache.getInstance().getOperator(operatorid);
            if (null == operator) {
                accountCacheService.getOneOperator(operatorid);
                operator = CMCCAccountCache.getInstance().getOperator(operatorid);
            }
        } else {
            // 关联独立的账号表
            operator = CMCCAccountCache.getInstance().getAccount(zid);
            if (null == operator) {
                accountCacheService.getOneAccount(zid);
                operator = CMCCAccountCache.getInstance().getAccount(zid);
            }
        }
        return operator;
    } 
    
    
    public CTCCOperator getCTCCOperator(Integer operatorid, Integer zid) {
        CTCCOperator operator = null;
        if (NOZID == zid) {
            operator = CTCCAccountCache.getInstance().getOperator(operatorid);
            if (null == operator) {
                accountCacheService.getOneOperator(operatorid);
                operator = CTCCAccountCache.getInstance().getOperator(operatorid);
            }
        } else {
            // 关联独立的账号表
            operator = CTCCAccountCache.getInstance().getAccount(zid);
            if (null == operator) {
                accountCacheService.getOneAccount(zid);
                operator = CTCCAccountCache.getInstance().getAccount(zid);
            }
        }
        return operator;
    }
    
    
    
    
    
    
    
    
    
}
