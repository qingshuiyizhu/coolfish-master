package org.com.coolfish.entity;

@SuppressWarnings("unused")
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

    // 重试次数
    private Integer retry = 0;

    // 是否存入数据库 否为0
    private Integer store = 0;

    // 是否处于沉默期 0不是 1是
    private Integer slient = 0;

    public MqJson(String tel, Integer operatorid, Integer zid, Double sumflow, Double useflow, Double per,
            Integer slient) {
        super();
        this.tel = tel;
        this.operatorid = operatorid;
        this.zid = zid;
        this.sumflow = sumflow;
        this.useflow = useflow;
        this.per = per;
        this.slient = slient;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getOperatorid() {
        return operatorid;
    }

    public void setOperatorid(Integer operatorid) {
        this.operatorid = operatorid;
    }

    public Integer getZid() {
        return zid;
    }

    public void setZid(Integer zid) {
        this.zid = zid;
    }

    public Double getSumflow() {
        return sumflow;
    }

    public void setSumflow(Double sumflow) {
        this.sumflow = sumflow;
    }

    public Double getUseflow() {
        return useflow;
    }

    public void setUseflow(Double useflow) {
        this.useflow = useflow;
    }

    public Double getPer() {
        return per;
    }

    public void setPer(Double per) {
        this.per = per;
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

    public void setStore(Integer store) {
        this.store = store;
    }

    public Integer getSlient() {
        return slient;
    }

    public void setSlient(Integer slient) {
        this.slient = slient;
    }

}
