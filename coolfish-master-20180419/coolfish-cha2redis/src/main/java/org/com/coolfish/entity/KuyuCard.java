package org.com.coolfish.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "kuyu_card")
@Entity
public class KuyuCard extends IdEntity implements Serializable {
  private static final long serialVersionUID = 1L;
    // 卡接入号
    private String tel;

    // operatorid IN (137,160,167) 对应的kuyu_operacor的主键ID
    private Integer operatorid;

    // 卡片基本类型 （1月卡， 2普通卡， 3行业卡）
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

    public KuyuCard(Long id, String tel, Integer operatorid) {
        super.setId(id);
        this.tel = tel;
        this.operatorid = operatorid;
    }

    public KuyuCard() {
        super();
    }

    @Override
    public String toString() {
        return "KuyuCard [id=" + super.getId() + ", tel=" + tel + ", operatorid=" + operatorid + "]";
    }

}
