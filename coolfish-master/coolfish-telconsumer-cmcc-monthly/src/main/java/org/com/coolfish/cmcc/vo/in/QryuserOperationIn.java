package org.com.coolfish.cmcc.vo.in;

import org.com.coolfish.cmcc.vo.in.content.QryuserContent;

/*
 * 物联网单卡实时流量查询 报文
 */
public class QryuserOperationIn extends OperationIn {
	private QryuserContent content;
	public QryuserContent getContent() {
		return content;
	}
	public void setContent(QryuserContent content) {
		this.content = content;
	}

}
