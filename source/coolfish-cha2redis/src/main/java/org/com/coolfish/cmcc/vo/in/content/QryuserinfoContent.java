package org.com.coolfish.cmcc.vo.in.content;
/*
 * 物联网用户基础信息及状态 查询参数
 */
@SuppressWarnings("unused")
public class QryuserinfoContent {
	// 地市编码
	private String ddr_city;
	private String groupid;
	// 物联网号码
	private String msisdn;

	public void setDdr_city(String ddr_city) {
		this.ddr_city = ddr_city;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public QryuserinfoContent(String ddr_city, String groupid, String msisdn) {
		super();
		this.ddr_city = ddr_city;
		this.groupid = groupid;
		this.msisdn = msisdn;
	}

	public QryuserinfoContent() {
		super();
	}

}
