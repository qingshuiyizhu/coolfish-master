package org.com.coolfish.common.message;

import java.util.Date;

public class MsisdnMessage {
    private Integer cardid;

    private String iphone;

    private String iccid;

    // 对应的kuyu_operacor的主键ID
    private Integer operatorid;

    private Integer operatorType;

    // 账号id
    private Integer zid;

    // 使用在用套餐之前的api流量 累计套餐
    private String beforeApiFlow;

    // 套餐剩余流量 累计套餐
    private String leftflow;

    // 总流量
    private String sumflow;

    // 已用流量
    private String useflow;

    // 流量显示比例
    private String per;

    private Date endTime;

    // 卡状态描述
    private String cardStatus;

    // 卡状态代码
    private String startusCode;

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getStartusCode() {
        return startusCode;
    }

    public void setStartusCode(String startusCode) {
        this.startusCode = startusCode;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getIphone() {
        this.iphone = iphone == null ? null : iphone.trim();
        return iphone;
    }

    public void setIphone(String iphone) {
        this.iphone = iphone;
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
        this.beforeApiFlow = beforeApiFlow == null ? "0.00" : beforeApiFlow.trim();
        return beforeApiFlow;
    }

    public void setBeforeApiFlow(String beforeApiFlow) {
        this.beforeApiFlow = beforeApiFlow;
    }

    public String getLeftflow() {
        this.leftflow = leftflow == null ? "0.00" : leftflow.trim();
        return leftflow;
    }

    public void setLeftflow(String leftFlow) {
        this.leftflow = leftFlow;
    }

    public String getSumflow() {
        this.sumflow = sumflow == null ? "0.00" : sumflow.trim();

        return sumflow;
    }

    public void setSumflow(String sumflow) {
        this.sumflow = sumflow;
    }

    public String getUseflow() {
        this.useflow = useflow == null ? "0.00" : useflow.trim();
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

    public Integer getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(Integer operatorType) {
        this.operatorType = operatorType;
    }

}
