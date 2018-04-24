package org.com.coolfish.cmcc.vo.in.content;

/*
实时查询集团成员单卡流量的参数
*/
public class DKLLCXContent {
	// 物联网号码
	private String service_number = "";
	// 查询月份 yyyymmdd
	private String cycle = "";

	public String getService_number() {
		return service_number;
	}

	public void setService_number(String service_number) {
		this.service_number = service_number;
	}

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public DKLLCXContent(String service_number, String cycle) {
		this.service_number = service_number;
		this.cycle = cycle;
	}

	public DKLLCXContent() {
	}
}
