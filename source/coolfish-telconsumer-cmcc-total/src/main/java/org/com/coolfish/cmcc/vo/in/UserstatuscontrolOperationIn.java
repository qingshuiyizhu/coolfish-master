package org.com.coolfish.cmcc.vo.in;

import org.com.coolfish.cmcc.vo.in.content.UserstatuscontrolContent;

/*
 * 物联网成员服务状态变更（语音、短信、上网） 报文
 */
public class UserstatuscontrolOperationIn extends OperationIn {
	private UserstatuscontrolContent content;
	public UserstatuscontrolContent getContent() {
		return content;
	}
	public void setContent(UserstatuscontrolContent content) {
		this.content = content;
	}

}
