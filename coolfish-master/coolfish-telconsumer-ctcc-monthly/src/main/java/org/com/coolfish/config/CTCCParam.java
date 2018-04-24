package org.com.coolfish.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CTCCParam {
    @Value("${coolfish.ctcc.url}")
    private String url;

    @Value("${coolfish.ctcc.user_id}")
    private String user_id;

    @Value("${coolfish.ctcc.password}")
    private String password;

    @Value("${coolfish.ctcc.keys}")
    private String keys;

    // 最大重试次数
    @Value("${coolfish.cotrol.remax}")
    private Integer remax;

    // 每次请求失败重试次数
    @Value("${coolfish.cotrol.evmax}")
    private Integer evmax;

    // 每天记录使用量的开始时间
    @Value("${coolfish.cotrol.logstartTime}")
    private String logstartTime;

    // 每天记录使用量的结束时间
    @Value("${coolfish.cotrol.logendTime}")
    private String  logendTime;

    public String getLogstartTime() {
        return logstartTime.trim();
    }

    public void setLogstartTime(String logstartTime) {
        this.logstartTime = logstartTime;
    }

    public String getLogendTime() {
        return logendTime.trim();
    }

    public void setLogendTime(String logendTime) {
        this.logendTime = logendTime;
    }

    public Integer getRemax() {
        return remax;
    }

    public Integer getEvmax() {
        return evmax;
    }

    public String getUrl() {
        return url;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getPassword() {
        return password;
    }

    public String getkey1() {
        return keys.substring(0, 3);
    }

    public String getkey2() {
        return keys.substring(3, 6);
    }

    public String getkey3() {
        return keys.substring(6, 9);
    }
}
