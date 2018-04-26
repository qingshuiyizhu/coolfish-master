package org.com.coolfish.common.webinterface.cmcc.xml.content;

@SuppressWarnings("unused")
public class QryuserContent {
    // 号码
    private String telnum = "";

    public void setTelnum(String telnum) {
        this.telnum = telnum;
    }

    public QryuserContent(String telnum) {
        super();
        this.telnum = telnum;
    }

    public QryuserContent() {
        super();
    }

}
