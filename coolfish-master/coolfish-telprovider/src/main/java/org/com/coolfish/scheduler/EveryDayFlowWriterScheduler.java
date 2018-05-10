package org.com.coolfish.scheduler;

import java.util.Set;

import org.com.coolfish.common.database.service.ComDBService;
import org.com.coolfish.common.message.MsisdnMessage;
import org.com.coolfish.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Slf4j
@Service
public class EveryDayFlowWriterScheduler {
    @Autowired
    private ComDBService databaseService;

    // 月套餐每日流量使用记录 每日0点执行
   // @Scheduled(cron = "0 0 0 * * ?")
    public void everyDayFlowWriter() {
        Jedis redis = new Jedis("10.31.76.24", 6379, 500000);
        redis.auth("coolfish888");
        redis.select(0);
        Set<String> keys = redis.keys("*");
        for (String key : keys) {
            MsisdnMessage msisdnMessage = null;
            try {
                String redisMessage = redis.get(key);
                msisdnMessage = JSON.parseObject(redisMessage, MsisdnMessage.class);
                log.info("Redis缓存获取信息：{}", redisMessage);
            } catch (Exception e) {

            }
            if (null != msisdnMessage) {
                databaseService.MonthlyEveryDayLogWriter(msisdnMessage);
            }
        }
        redis.disconnect();

    }

}
