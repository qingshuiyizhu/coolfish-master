package org.com.coolfish.common.model;

public class CMCCOperator {
    private Integer id;

    private String operator_name;

    private String appid;

    private String token;

    private String groupid;

    private String password;

    private String ddrcity;

    private String appid2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDdrcity(String ddrcity) {
        this.ddrcity = ddrcity;
    }

    public void setAppid2(String appid2) {
        this.appid2 = appid2;
    }

    public String getAppid() {
        return appid;
    }

    public String getToken() {
        return token;
    }

    public String getGroupid() {
        return groupid;
    }

    public String getPassword() {
        return password;
    }

    public String getDdrcity() {
        return ddrcity;
    }

    public String getAppid2() {
        return appid2;
    }

    @Override
    public String toString() {
        return "CMCCOperator [appid=" + appid + ", token=" + token + ", groupid=" + groupid + ", password="
                + password + ", ddrcity=" + ddrcity + ", appid2=" + appid2 + "]";
    }

}
