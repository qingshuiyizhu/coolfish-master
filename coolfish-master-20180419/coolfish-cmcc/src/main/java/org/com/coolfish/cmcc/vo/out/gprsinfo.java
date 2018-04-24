package org.com.coolfish.cmcc.vo.out;

public class gprsinfo {
	// 用户组ID
	private String user_id;
	// 流量池总量（KB）
	private Long max_value;
	// 流量池使用量（KB）
	private Long cumulate_value;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Long getMax_value() {
		return max_value;
	}

	public void setMax_value(Long max_value) {
		this.max_value = max_value;
	}

	public Long getCumulate_value() {
		return cumulate_value;
	}

	public void setCumulate_value(Long cumulate_value) {
		this.cumulate_value = cumulate_value;
	}

}
