package org.com.coolfish.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Table(name = "kuyu_flow_detail")
@Entity
public class KuyuFlowDetail {
    @Id
    @GeneratedValue
    private Integer id;

    // 号码id
    private Long cardid;

    // 号码
    private String tel;

    // 当晚请求api得到的使用量（KB）
    private BigDecimal apiflow;

    // 记录的时间
    private Date time;

    // 当日使用量
    private BigDecimal dayflow;

    // 切换到该套餐到现在的总使用量
    private BigDecimal useflow;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCardid() {
        return cardid;
    }

    public void setCardid(Long cardid) {
        this.cardid = cardid;
    }

    public BigDecimal getApiflow() {
        return apiflow;
    }

    public void setApiflow(BigDecimal apiflow) {
        this.apiflow = apiflow;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public BigDecimal getDayflow() {
        return dayflow;
    }

    public void setDayflow(BigDecimal dayflow) {
        this.dayflow = dayflow;
    }

    public BigDecimal getUseflow() {
        return useflow;
    }

    public void setUseflow(BigDecimal useflow) {
        this.useflow = useflow;
    }
}