package org.com.coolfish.cmcc.vo.in.content;

/*
 * 物联网动态流量池实时情况查询 参数
 */
@SuppressWarnings("unused")
public class DTLLCContent {
	// 查询类型 0：eccode是指物联网平台集团ID 1：eccode 是指省内集团ID
	
	private String qrytype = "0";
	// 客户编号 取值是数字串
	private String eccode = "";
	// 查询月份 yyyymmdd
	private String cycle = "";

	public DTLLCContent(String qrytype, String eccode, String cycle) {
		super();
		this.qrytype = qrytype;
		this.eccode = eccode;
		this.cycle = cycle;
	}

	public DTLLCContent() {
		super();

	}

	public void setQrytype(String qrytype) {
		this.qrytype = qrytype;
	}

	public void setEccode(String eccode) {
		this.eccode = eccode;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

}
