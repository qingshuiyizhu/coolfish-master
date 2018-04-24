package org.com.coolfish.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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

    // 总流量
    private BigDecimal sumflow;

    // 已用流量
    private BigDecimal useflow;

    // 流量显示比例
    private BigDecimal per;

    // 卡片状态
    private Integer card_status;

    private Integer card_type;

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

    public KuyuCard(Integer operator_type, String tel, Integer operatorid, Integer zid, BigDecimal sumflow,
            BigDecimal useflow, BigDecimal per, Integer card_status, Integer type) {
        super();
        this.operator_type = operator_type;
        this.tel = tel;
        this.operatorid = operatorid;
        this.zid = zid;
        this.sumflow = sumflow;
        this.useflow = useflow;
        this.per = per;
        this.card_status = card_status;
        this.type = type;
    }

    @Override
    public String toString() {
        return "KuyuCard [operator_type=" + operator_type + ", tel=" + tel + ", operatorid=" + operatorid
                + ", zid=" + zid + ", sumflow=" + sumflow + ", useflow=" + useflow + ", per=" + per
                + ", card_status=" + card_status + ", card_type=" + card_type + ", type=" + type + "]";
    }

}
