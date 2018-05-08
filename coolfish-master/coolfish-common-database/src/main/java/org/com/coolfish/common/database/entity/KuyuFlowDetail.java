package org.com.coolfish.common.database.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "kuyu_flow_detail")
@Entity
public class KuyuFlowDetail extends IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    // 物联网卡id
    private Integer cardid;

    // 物联网卡号码
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

    public Integer getCardid() {
        return cardid;
    }

    public void setCardid(Integer cardid) {
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

    public KuyuFlowDetail() {
        super();
    }

    public KuyuFlowDetail(BigDecimal apiflow, Date time) {
        super();
        this.apiflow = apiflow;
        this.time = time;
    }

    @Override
    public String toString() {
        return "KuyuFlowDetail [id=" +id + ", cardid=" + cardid + ", tel=" + tel + ", apiflow="
                + apiflow + ", time=" + time + ", dayflow=" + dayflow + ", useflow=" + useflow + "]";
    }

}