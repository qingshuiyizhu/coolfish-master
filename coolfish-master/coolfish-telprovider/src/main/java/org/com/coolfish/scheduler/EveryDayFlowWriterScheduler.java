package org.com.coolfish.scheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.com.coolfish.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EveryDayFlowWriterScheduler {

    @Autowired
    private RedisService redisService;

    public void everyDayFlowWriter() {
        // redisService.

    }
public static void main(String[] args) {
    get();
}
    public static void get() {
        System.out.println("-----------------");
        Jedis redis = new Jedis("39.108.19.148", 6379, 500000);
        redis.auth("coolfish888");
        redis.select(0);
        // 使用pipeline hmset
        Pipeline p = redis.pipelined();
        // hmget
        Set<String> keys = redis.keys("*");
        // 集合类的通用遍历方式, 从很早的版本就有, 用迭代器迭代
        // 直接使用Jedis hgetall

        Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();

        for (String key : keys) {
            result.put(key, redis.hgetAll(key));
        }

        // 使用pipeline hgetall
        Map<String, Response<Map<String, String>>> responses = new HashMap<String, Response<Map<String, String>>>(
                keys.size());
        result.clear();

        for (String key : keys) {
            responses.put(key, p.hgetAll(key));
        }
        p.sync();
        for (String k : responses.keySet()) {
            result.put(k, responses.get(k).get());
        }

        System.out.println("result size:[" + result.size() + "] ..");

        for (String key : keys) {
            result.put(key, redis.hgetAll(key));
        }
        redis.disconnect();
    }

}
