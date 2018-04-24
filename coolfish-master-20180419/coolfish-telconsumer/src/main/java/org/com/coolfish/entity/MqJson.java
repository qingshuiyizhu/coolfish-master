package org.com.coolfish.entity;

public class MqJson {
    private String tel;

    // 对应的kuyu_operacor的主键ID
    private Integer operatorid;

    // 账号id
    private Integer zid;

    // 总流量
    private Double sumflow;

    // 已用流量
    private Double useflow;

    // 流量显示比例
    private Double per;

    private Integer retry = 0;

    private Integer store = 0;

    public String getTel() {
        return tel;
    }

    public Integer getOperatorid() {
        return operatorid;
    }

    public Integer getZid() {
        return zid;
    }

    public Double getSumflow() {
        return sumflow;
    }

    public void setUseflow(Double useflow) {
        this.useflow = useflow;
    }

    public Double getUseflow() {
        return useflow;
    }

    public Double getPer() {
        return per;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public Integer getStore() {
        return store;
    }

    public MqJson(String tel, Integer operatorid, Integer zid, Double sumflow, Double useflow,
            Double per) {
        super();
        this.tel = tel;
        this.operatorid = operatorid;
        this.zid = zid;
        this.sumflow = sumflow;
        this.useflow = useflow;
        this.per = per;
    }

    @Override
    public String toString() {
        return "MqJson [tel=" + tel + ", operatorid=" + operatorid + ", zid=" + zid + ", sumflow=" + sumflow
                + ", useflow=" + useflow + ", per=" + per + ", retry=" + retry + ", store=" + store + "]";
    }
    
    
}
