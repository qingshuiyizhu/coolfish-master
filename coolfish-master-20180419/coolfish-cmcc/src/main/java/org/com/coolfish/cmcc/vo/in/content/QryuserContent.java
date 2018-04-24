package org.com.coolfish.cmcc.vo.in.content;

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
