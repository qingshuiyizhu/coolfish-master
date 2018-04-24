package org.com.coolfish.cmcc.vo.out;

/**
 * 应答报文 返回参数
 */
public class OperationOut {
	// 请求流水号
	private String req_seq;
	// 响应流水号
	private String resp_seq;
	// 应答时间 YYYYMMDDHHMMSS
	private String resp_time;
	// 应急标志
	private Integer emergency_status;
	 // 应答
	private Response response; 

	public String getReq_seq() {
		return req_seq;
	}

	public void setReq_seq(String req_seq) {
		this.req_seq = req_seq;
	}

	public String getResp_seq() {
		return resp_seq;
	}

	public void setResp_seq(String resp_seq) {
		this.resp_seq = resp_seq;
	}

	public String getResp_time() {
		return resp_time;
	}

	public void setResp_time(String resp_time) {
		this.resp_time = resp_time;
	}

	public Integer getEmergency_status() {
		return emergency_status;
	}

	public void setEmergency_status(Integer emergency_status) {
		this.emergency_status = emergency_status;
	}

 public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}
 
}
