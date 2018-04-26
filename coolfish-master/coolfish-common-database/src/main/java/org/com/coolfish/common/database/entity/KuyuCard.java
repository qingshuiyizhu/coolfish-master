package org.com.coolfish.common.database.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "kuyu_card")
@Entity
public class KuyuCard extends IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    // 卡片所属三大运营商类型（1移动，2电信，3联通）
    private Integer operator_type;

    // 卡接入号
    private String tel;

    // 对应的kuyu_operacor的主键ID
    private Integer operatorid;

    // 账号id
    private Integer zid;

    private Date starttime;

    // 总流量
    private BigDecimal sumflow;

    // 已用流量
    private BigDecimal useflow;

    // 流量显示比例
    private BigDecimal per;

    // 卡片状态
    private Integer card_status;

    private Integer card_type;

    // 充值次数
    private Integer frequency;

    // 使用在用套餐之前的查询的流量
    private BigDecimal beforeApiFlow;

    @Transient
    public BigDecimal getBeforeApiFlow() {
        return beforeApiFlow;
    }

    public void setBeforeApiFlow(BigDecimal beforeApiFlow) {
        this.beforeApiFlow = beforeApiFlow;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    // 卡片基本类型 （卡片基本类型 （1月套餐， 2累计套餐， 30月租卡，4空套餐 ，5当月套餐）
    @Column(name = "type")
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getOperator_type() {
        return operator_type;
    }

    public void setOperator_type(Integer operator_type) {
        this.operator_type = operator_type;
    }

    public Integer getZid() {
        return zid;
    }

    public void setZid(Integer zid) {
        this.zid = zid;
    }

    public BigDecimal getSumflow() {
        return sumflow;
    }

    public void setSumflow(BigDecimal sumflow) {
        this.sumflow = sumflow;
    }

    public BigDecimal getUseflow() {
        return useflow;
    }

    public void setUseflow(BigDecimal useflow) {
        this.useflow = useflow;
    }

    public BigDecimal getPer() {
        return per;
    }

    public void setPer(BigDecimal per) {
        this.per = per;
    }

    public Integer getCard_status() {
        return card_status;
    }

    public void setCard_status(Integer card_status) {
        this.card_status = card_status;
    }

    public Integer getCard_type() {
        return card_type;
    }

    public void setCard_type(Integer card_type) {
        this.card_type = card_type;
    }

    public KuyuCard(Integer id, Integer operator_type, String tel, Integer operatorid, Integer zid,
            Date starttime, BigDecimal sumflow, BigDecimal useflow, BigDecimal per, Integer card_status,
            Integer card_type, Integer frequency, BigDecimal beforeApiFlow, Integer type) {
        super();
        this.setId(id);
        this.operator_type = operator_type;
        this.tel = tel;
        this.operatorid = operatorid;
        this.zid = zid;
        this.starttime = starttime;
        this.sumflow = sumflow;
        this.useflow = useflow;
        this.per = per;
        this.card_status = card_status;
        this.card_type = card_type;
        this.frequency = frequency;
        this.beforeApiFlow = beforeApiFlow;
        this.type = type;
    }

    public KuyuCard(Integer id, Integer operator_type, String tel, Integer operatorid, Integer zid,
            BigDecimal sumflow, BigDecimal useflow, BigDecimal per, Integer card_status, Integer type,
            Integer frequency) {
        super();
        this.setId(id);
        this.operator_type = operator_type;
        this.tel = tel;
        this.operatorid = operatorid;
        this.zid = zid;
        this.sumflow = sumflow;
        this.useflow = useflow;
        this.per = per;
        this.card_status = card_status;
        this.type = type;
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "KuyuCard [operator_type=" + operator_type + ", tel=" + tel + ", operatorid=" + operatorid
                + ", zid=" + zid + ", starttime=" + starttime + ", sumflow=" + sumflow + ", useflow="
                + useflow + ", per=" + per + ", card_status=" + card_status + ", card_type=" + card_type
                + ", frequency=" + frequency + ", beforeApiFlow=" + beforeApiFlow + ", type=" + type
                + ", id()=" + getId() + "]";
    }

}
