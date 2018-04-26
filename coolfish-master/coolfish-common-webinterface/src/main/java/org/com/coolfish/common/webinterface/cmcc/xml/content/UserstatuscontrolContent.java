package org.com.coolfish.common.webinterface.cmcc.xml.content;

/*
 * 物联网成员服务状态变更 请求参数
 * 
 */
@SuppressWarnings("unused")
public class UserstatuscontrolContent {
	// 集团编码
	private String groupid="";
	// 物联网号码
	private String msisdn="";
	//需要变更成的结果状态
	private String SUB_SERVICE_STATUS="";
	// 服务类型
	private String service="";
	// 操作类型
	private String oprtype="";
	
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public void setService(String service) {
		this.service = service;
	}

	public void setOprtype(String oprtype) {
		this.oprtype = oprtype;
	}

	public UserstatuscontrolContent(String groupid, String msisdn, String service, String oprtype) {
		super();
		this.groupid = groupid;
		this.msisdn = msisdn;
		this.service = service;
		this.oprtype = oprtype;
	}
	
	

	public UserstatuscontrolContent(String groupid, String msisdn, String sUB_SERVICE_STATUS, String service,
			String oprtype) {
		super();
		this.groupid = groupid;
		this.msisdn = msisdn;
		SUB_SERVICE_STATUS = sUB_SERVICE_STATUS;
		this.service = service;
		this.oprtype = oprtype;
	}

	public UserstatuscontrolContent() {
		super();
	}

}
