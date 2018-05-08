package org.com.coolfish.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
@Configuration
@PropertySource(value = {"classpath:coolfish.properties"},ignoreResourceNotFound=true)
public class ParamConfig {

    // 最大重试次数
    @Value("${coolfish.cotrol.remax}")
    private Integer remax;

    // 每天记录使用量的开始时间
    @Value("${coolfish.cotrol.logstartTime}")
    private String logstartTime;

    // 每天记录使用量的结束时间
    @Value("${coolfish.cotrol.logendTime}")
    private String logendTime;

    public Integer getRemax() {
        return remax;
    }

    public void setRemax(Integer remax) {
        this.remax = remax;
    }

    public String getLogstartTime() {
        return logstartTime;
    }

    public void setLogstartTime(String logstartTime) {
        this.logstartTime = logstartTime;
    }

    public String getLogendTime() {
        return logendTime;
    }

    public void setLogendTime(String logendTime) {
        this.logendTime = logendTime;
    }

}
