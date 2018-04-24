package org.com.coolfish.message;

import java.util.Date;

public class MsisdnMessage {

    private String tel;

    // 对应的kuyu_operacor的主键ID
    private Integer operatorid;

    // 账号id
    private Integer zid;

    // 使用在用套餐之前的api流量
    private String beforeApiFlow;

    // 套餐剩余流量
    private String leftFlow;

    // 总流量
    private String sumflow;

    // 已用流量
    private String useflow;

    // 流量显示比例
    private String per;

    // 断网重试总次数
    private Integer retry = 0;

    // 是否存入数据库 否为0
    private Integer store = 0;

    // 是否处于沉默期 0不是 1是
    private Integer slient = 0;

    private Long cardid;

    private Date endTime;

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public MsisdnMessage() {
    }

    public MsisdnMessage(String tel, Integer operatorid, Integer zid, String sumflow, String useflow,
            String per, Integer slient, Long cardid, String beforeApiFlow, String leftFlow, Date endTime,
            Integer store) {
        super();
        this.tel = tel;
        this.operatorid = operatorid;
        this.zid = zid;
        this.sumflow = sumflow;
        this.useflow = useflow;
        this.per = per;
        this.slient = slient;
        this.cardid = cardid;
        this.beforeApiFlow = beforeApiFlow;
        this.leftFlow = leftFlow;
        this.endTime = endTime;
        this.store = store;
    }

    public MsisdnMessage(String tel, Integer operatorid, Integer zid, String beforeApiFlow, String leftFlow,
            String sumflow, String useflow, String per, Integer retry, Integer store, Integer slient,
            Long cardid) {
        super();
        this.tel = tel;
        this.operatorid = operatorid;
        this.zid = zid;
        this.beforeApiFlow = beforeApiFlow;
        this.leftFlow = leftFlow;
        this.sumflow = sumflow;
        this.useflow = useflow;
        this.per = per;
        this.retry = retry;
        this.store = store;
        this.slient = slient;
        this.cardid = cardid;
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

    public String getBeforeApiFlow() {
        return beforeApiFlow;
    }

    public void setBeforeApiFlow(String beforeApiFlow) {
        this.beforeApiFlow = beforeApiFlow;
    }

    public String getLeftFlow() {
        return leftFlow;
    }

    public void setLeftFlow(String leftFlow) {
        this.leftFlow = leftFlow;
    }

    public String getSumflow() {
        return sumflow;
    }

    public void setSumflow(String sumflow) {
        this.sumflow = sumflow;
    }

    public String getUseflow() {
        return useflow;
    }

    public void setUseflow(String useflow) {
        this.useflow = useflow;
    }

    public String getPer() {
        return per;
    }

    public void setPer(String per) {
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

    public Long getCardid() {
        return cardid;
    }

    public void setCardid(Long cardid) {
        this.cardid = cardid;
    }

    @Override
    public String toString() {
        return "MsisdnMessage [tel=" + tel + ", operatorid=" + operatorid + ", zid=" + zid
                + ", beforeApiFlow=" + beforeApiFlow + ", leftFlow=" + leftFlow + ", sumflow=" + sumflow
                + ", useflow=" + useflow + ", per=" + per + ", retry=" + retry + ", store=" + store
                + ", slient=" + slient + ", cardid=" + cardid + ", endTime=" + endTime + "]";
    }

}
