package org.com.coolfish.cmcc.vo.in.content;

public class QrystatusContent {
	// 集团编码
	private String groupid = "";
	// 号码
	private String telnum = "";

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getTelnum() {
		return telnum;
	}

	public void setTelnum(String telnum) {
		this.telnum = telnum;
	}

	public QrystatusContent(String groupid, String telnum) {
		super();
		this.groupid = groupid;
		this.telnum = telnum;
	}

	public QrystatusContent() {
		super();
		// TODO Auto-generated constructor stub
	}

}
