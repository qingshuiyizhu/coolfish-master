package org.coolfish.redis.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "kuyu_card")
@Entity
public class KuyuCard {
	@Id
	@GeneratedValue
	private Long id;
	// 卡接入号
	private String tel;
	// operatorid IN (137,160,167) 对应的kuyu_operacor的主键ID
	private Integer operatorid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		super();
		this.id = id;
		this.tel = tel;
		this.operatorid = operatorid;
	}

	public KuyuCard() {
		super();
	}

	@Override
	public String toString() {
		return "KuyuCard [id=" + id + ", tel=" + tel + ", operatorid=" + operatorid + "]";
	}

}
