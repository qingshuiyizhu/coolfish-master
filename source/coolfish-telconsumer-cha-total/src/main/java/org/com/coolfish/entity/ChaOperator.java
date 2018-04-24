package org.com.coolfish.entity;

public class ChaOperator {

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

    public ChaOperator() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ChaOperator(String usercode, String password, String keys) {
        super();
        this.usercode = usercode;
        this.password = password;
        this.keys = keys;
    }
    
}
