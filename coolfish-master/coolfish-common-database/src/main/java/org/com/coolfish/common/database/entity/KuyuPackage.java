package org.com.coolfish.common.database.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "kuyu_package")
@Entity
public class KuyuPackage extends IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer uid;

    private String name;

    private Integer cardType;

    private Boolean packageType;

    private String text;

    private BigDecimal oldPrice;

    private BigDecimal price;

    private Date addtime;

    private String packageid;

    private Integer sort;

    private Integer status;

    private BigDecimal flow;

    private Integer usetime;

    private Integer type;

    private Integer operatorid;

    private Boolean stackable;

    private Boolean base;

    private Byte isDis;

    private Integer pStarttime;

    private Integer pEndtime;

    private Integer count;

    private Byte isInfinite;

    private Boolean hide;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public Boolean getPackageType() {
        return packageType;
    }

    public void setPackageType(Boolean packageType) {
        this.packageType = packageType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }

    public BigDecimal getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getPackageid() {
        return packageid;
    }

    public void setPackageid(String packageid) {
        this.packageid = packageid == null ? null : packageid.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getFlow() {
        return flow;
    }

    public void setFlow(BigDecimal flow) {
        this.flow = flow;
    }

    public Integer getUsetime() {
        return usetime;
    }

    public void setUsetime(Integer usetime) {
        this.usetime = usetime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOperatorid() {
        return operatorid;
    }

    public void setOperatorid(Integer operatorid) {
        this.operatorid = operatorid;
    }

    public Boolean getStackable() {
        return stackable;
    }

    public void setStackable(Boolean stackable) {
        this.stackable = stackable;
    }

    public Boolean getBase() {
        return base;
    }

    public void setBase(Boolean base) {
        this.base = base;
    }

    public Byte getIsDis() {
        return isDis;
    }

    public void setIsDis(Byte isDis) {
        this.isDis = isDis;
    }

    public Integer getpStarttime() {
        return pStarttime;
    }

    public void setpStarttime(Integer pStarttime) {
        this.pStarttime = pStarttime;
    }

    public Integer getpEndtime() {
        return pEndtime;
    }

    public void setpEndtime(Integer pEndtime) {
        this.pEndtime = pEndtime;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Byte getIsInfinite() {
        return isInfinite;
    }

    public void setIsInfinite(Byte isInfinite) {
        this.isInfinite = isInfinite;
    }

    public Boolean getHide() {
        return hide;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }
}