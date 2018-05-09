package org.com.coolfish.common.spi.initialize;

import org.com.coolfish.common.spi.service.AccountCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 这里通过设定value的值来指定执行顺序
 */
@Slf4j
@Component
@Order(value = 1)
public class MyApplicationRunner implements ApplicationRunner{

    @Autowired
    private AccountCacheService accountCacheService;
  
    @Override
    public void run(ApplicationArguments var1) throws Exception{
        log.info("########################加载供应商账号信息###########################");
        accountCacheService.load();
     
    }

}