package org.com.coolfish.common.model;

public class CTCCOperator {

    private String usercode;

    private String password;

    private String keys;

    public String getUsercode() {
        return usercode;
    }

    public String getPassword() {
        return password;
    }

    public String getkey1() {
        return keys.substring(0, 3);
    }

    public String getkey2() {
        return keys.substring(3, 6);
    }

    public String getkey3() {
        return keys.substring(6, 9);
    }

    public CTCCOperator() {
        super();
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public CTCCOperator(String usercode, String password, String keys) {
        super();
        this.usercode = usercode;
        this.password = password;
        this.keys = keys;
    }

    @Override
    public String toString() {
        return "CTCCOperator [usercode=" + usercode + ", password=" + password + ", keys=" + keys + "]";
    }

    
}
