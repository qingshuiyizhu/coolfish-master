package org.com.coolfish.common.model;

public class UtilBean {
    // 卡的id
    protected Integer cardId;

    // 号码
    protected String iphone;

    protected String iccid;

    // 供应商类型
    protected Integer operatorType;

    // 供应商id
    protected Integer operatorid;

    protected Integer zid;

    // 是否返回解析后数据1：是 0 否 ，默认是否
    protected Integer self = 0;

    // 第三方返回的结果信息
    protected String resultMsg;
    
    //返回结果状态码
    private String resultCode;

    // 解析后的结果
    protected String analyze;

    // 请求是否成功 默认成功1 -1 错误
    protected Integer success = 1;

    // 来源
    protected String source ;

    // 操作者
    private String operatorName;

    // 操作原因
    private String actionReason;
   
    //流水号
    
    private String serialNumber;
    
    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getIphone() {
        return iphone;
    }

    public void setIphone(String iphone) {
        this.iphone = iphone;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public Integer getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(Integer operatorType) {
        this.operatorType = operatorType;
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

    public Integer getSelf() {
        return self;
    }

    public void setSelf(Integer self) {
        this.self = self;
    }
 
    public String getAnalyze() {
        return analyze;
    }

    public void setAnalyze(String analyze) {
        this.analyze = analyze;
    }
 

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getActionReason() {
        return actionReason;
    }

    public void setActionReason(String actionReason) {
        this.actionReason = actionReason;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() {
        return "UtilBean [cardId=" + cardId + ", iphone=" + iphone + ", iccid=" + iccid + ", operatorType="
                + operatorType + ", operatorid=" + operatorid + ", zid=" + zid + ", self=" + self
                + ", resultMsg=" + resultMsg + ", resultCode=" + resultCode + ", analyze=" + analyze
                + ", success=" + success + ", source=" + source + ", operatorName=" + operatorName
                + ", actionReason=" + actionReason + ", serialNumber=" + serialNumber + "]";
    }

    
    
 
}
