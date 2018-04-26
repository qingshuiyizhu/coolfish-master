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

    // 每天记录使用量的开始时间
    @Value("${coolfish.cotrol.logstartTime}")
    private String logstartTime;

    // 每天记录使用量的结束时间
    @Value("${coolfish.cotrol.logendTime}")
    private String logendTime;

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
