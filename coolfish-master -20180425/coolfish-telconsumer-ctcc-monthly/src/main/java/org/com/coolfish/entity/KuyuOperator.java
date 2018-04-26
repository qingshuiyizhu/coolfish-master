package org.com.coolfish.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 
 * @author LINGHUI 运营商管理表
 */
@Table(name = "kuyu_operator")
@Entity
public class KuyuOperator extends IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    // 自主命名的账号名称
    private String name;

    // 运营商的类型（1移动，2电信，3联通，4其他）
    private Integer operator_type;

    // 移动对应广东企业编码,电信对应账号
    private String usercode;

    // 移动对应广东appkey值，电信对应秘钥
    private String keys;

    // 移动对应重庆密码，电信对应密码
    private String password;

    // 移动对应广东secret值
    private String secret;

    // 移动对应重庆appid值
    private String appid;

    private String text;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOperator_type() {
        return operator_type;
    }

    public void setOperator_type(Integer operator_type) {
        this.operator_type = operator_type;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    @Lob
    // @Basic(fetch = FetchType.LAZY)
    // @Type(type = "text")
    @Column(name = "text", columnDefinition = "text", nullable = true)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public KuyuOperator(Long id, String text) {
        this.setId(id);
        this.text = text;
    }

    public KuyuOperator() {
        super();
    }

    @Override
    public String toString() {
        return "KuyuOperator [name=" + name + ", operator_type=" + operator_type + ", usercode=" + usercode
                + ", keys=" + keys + ", password=" + password + ", secret=" + secret + ", appid=" + appid
                + ", text=" + text + ", id=" + getId() + "]";
    }

}
