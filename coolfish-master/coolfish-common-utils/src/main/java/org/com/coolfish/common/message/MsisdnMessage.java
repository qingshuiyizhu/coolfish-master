package org.com.coolfish.common.message;

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
    private String leftflow;

    // 总流量
    private String sumflow;

    // 已用流量
    private String useflow;

    // 流量显示比例
    private String per;

    // 重试次数
    private Integer retry;

    // 是否处于沉默期 0不是 1是
    private Integer slient = 0;

    private Integer cardid;

    private Date endTime;

    private String cardStatus;

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTel() {
        this.tel = tel == null ? null : tel.trim();
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
        this.beforeApiFlow = beforeApiFlow == null ? null : beforeApiFlow.trim();
        return beforeApiFlow;
    }

    public void setBeforeApiFlow(String beforeApiFlow) {
        this.beforeApiFlow = beforeApiFlow;
    }

    public String getLeftflow() {
        this.leftflow = leftflow == null ? null : leftflow.trim();
        return leftflow;
    }

    public void setLeftflow(String leftFlow) {
        this.leftflow = leftFlow;
    }

    public String getSumflow() {
        this.sumflow = sumflow == null ? null : sumflow.trim();

        return sumflow;
    }

    public void setSumflow(String sumflow) {
        this.sumflow = sumflow;
    }

    public String getUseflow() {
        this.useflow = useflow == null ? null : useflow.trim();
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

    public Integer getSlient() {
        return slient;
    }

    public void setSlient(Integer slient) {
        this.slient = slient;
    }

    public Integer getCardid() {
        return cardid;
    }

    public void setCardid(Integer cardid) {
        this.cardid = cardid;
    }

    public String getCardStatus() {
        this.cardStatus = cardStatus == null ? null : cardStatus.trim();
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public MsisdnMessage() {
        super();
    }

    public MsisdnMessage(String tel, Integer operatorid, Integer zid, String beforeApiFlow, String leftFlow,
            String sumflow, String useflow, String per, Integer retry, Integer slient, Integer cardid,
            Date endTime, String cardStatus) {
        super();
        this.tel = tel;
        this.operatorid = operatorid;
        this.zid = zid;
        this.beforeApiFlow = beforeApiFlow;
        this.leftflow = leftFlow;
        this.sumflow = sumflow;
        this.useflow = useflow;
        this.per = per;
        this.retry = retry;
        this.slient = slient;
        this.cardid = cardid;
        this.endTime = endTime;
        this.cardStatus = cardStatus;
    }

}
