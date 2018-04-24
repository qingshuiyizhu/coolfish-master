package org.com.coolfish.cmcc.vo.in;

import org.com.coolfish.cmcc.vo.in.content.QryuserinfoContent;

/*
 * 物联网单卡实时流量查询 报文
 */
public class QryuserinfoOperationIn extends OperationIn {
	private QryuserinfoContent content;
	public QryuserinfoContent getContent() {
		return content;
	}
	public void setContent(QryuserinfoContent content) {
		this.content = content;
	}

}
