package org.com.coolfish.cmcc.vo.in;

/*
 * 请求报文头 参数
 * Author:LINGHUI
 */
public class OperationIn {
	// 接口服务名
	private String process_code = "";
	// 应用标识
	private String app_id = "";
	// 令牌
	private String access_token = "";
	//
	private String sign = "";
	// 验证码
	private String verify_code = "";
	// 请求类型
	private String req_type = "";
	// 终端标识
	private String terminal_id = "";
	// 业务流水号
	private String accept_seq = "";
	// 请求流水号
	private String req_seq = "";
	// 请求时间 YYYYMMDDHHMMSS
	private String req_time = "";

	public String getProcess_code() {
		return process_code;
	}

	public void setProcess_code(String process_code) {
		this.process_code = process_code;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getVerify_code() {
		return verify_code;
	}

	public void setVerify_code(String verify_code) {
		this.verify_code = verify_code;
	}

	public String getReq_type() {
		return req_type;
	}

	public void setReq_type(String req_type) {
		this.req_type = req_type;
	}

	public String getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(String terminal_id) {
		this.terminal_id = terminal_id;
	}

	public String getAccept_seq() {
		return accept_seq;
	}

	public void setAccept_seq(String accept_seq) {
		this.accept_seq = accept_seq;
	}

	public String getReq_seq() {
		return req_seq;
	}

	public void setReq_seq(String req_seq) {
		this.req_seq = req_seq;
	}

	public String getReq_time() {
		return req_time;
	}

	public void setReq_time(String req_time) {
		this.req_time = req_time;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
