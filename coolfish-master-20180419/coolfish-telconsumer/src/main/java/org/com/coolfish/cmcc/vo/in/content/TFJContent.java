package org.com.coolfish.cmcc.vo.in.content;

/*
 * 物联网集团成员停复机 请求参数内容
 * 
 */
@SuppressWarnings("unused")
public class TFJContent {
	// 集团编码
	private String groupid="";
	// 号码
	private String telnum="";
	// 操作类型
	private String oprtype="";
	// 操作原因
	private String reason="";

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public void setTelnum(String telnum) {
		this.telnum = telnum;
	}

	public void setOprtype(String oprtype) {
		this.oprtype = oprtype;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public TFJContent(String groupid, String telnum, String oprtype, String reason) {
		super();
		this.groupid = groupid;
		this.telnum = telnum;
		this.oprtype = oprtype;
		this.reason = reason;
	}

	public TFJContent() {
	}

}
