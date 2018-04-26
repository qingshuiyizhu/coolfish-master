package org.com.coolfish.common.webinterface.cmcc.xml.content;

/*
 * 
 * 受理 测试期强制结束  请求参数
 */
@SuppressWarnings("unused")
public class SuspendtestContent {
	// 集团编码
	private String groupid="";
	// 号码
	private String telnum="";

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public void setTelnum(String telnum) {
		this.telnum = telnum;
	}

	public SuspendtestContent(String groupid, String telnum) {
		super();
		this.groupid = groupid;
		this.telnum = telnum;
	}

	public SuspendtestContent() {
		super();
	}

}
