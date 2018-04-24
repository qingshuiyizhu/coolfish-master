package org.com.coolfish.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CMCCParam {
    @Value("${coolfish.cmcc.url}")
    private String url;

    @Value("${coolfish.cmcc.appid}")
    private String appid;

    @Value("${coolfish.cmcc.token}")
    private String token;

    @Value("${coolfish.cmcc.groupid}")
    private String groupid;

    @Value("${coolfish.cmcc.password}")
    private String password;

    @Value("${coolfish.cmcc.ddrcity}")
    private String ddrcity;

    @Value("${coolfish.cmcc.appid2}")
    private String appid2;

    @Value("${coolfish.cmcc.threadNumber}")
    private Integer threadNumber;

    // 最大重试次数
    @Value("${coolfish.cotrol.remax}")
    private Integer remax;

    // 每次请求失败重试次数
    @Value("${coolfish.cotrol.evmax}")
    private Integer evmax;

    public Integer getRemax() {
        return remax;
    }

    public Integer getEvmax() {
        return evmax;
    }

    public Integer getThreadNumber() {
        return threadNumber;
    }

    public String getUrl() {
        return url;
    }

    public String getAppid() {
        return appid;
    }

    public String getToken() {
        return token;
    }

    public String getGroupid() {
        return groupid;
    }

    public String getPassword() {
        return password;
    }

    public String getDdrcity() {
        return ddrcity;
    }

    public String getAppid2() {
        return appid2;
    }
}