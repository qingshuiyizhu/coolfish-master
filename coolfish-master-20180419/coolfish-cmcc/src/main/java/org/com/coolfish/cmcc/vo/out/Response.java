package org.com.coolfish.cmcc.vo.out;

public class Response {
	// 应答类型
	private Integer resp_type;
	// 应答代码
	private Integer resp_code;
	// 应答描述
	private String resp_desc;

	public Integer getResp_type() {
		return resp_type;
	}

	public void setResp_type(Integer resp_type) {
		this.resp_type = resp_type;
	}

	public Integer getResp_code() {
		return resp_code;
	}

	public void setResp_code(Integer resp_code) {
		this.resp_code = resp_code;
	}

	public String getResp_desc() {
		return resp_desc;
	}

	public void setResp_desc(String resp_desc) {
		this.resp_desc = resp_desc;
	}

}
