package org.com.coolfish.common.webinterface.cmcc.xml.in;

import org.com.coolfish.common.webinterface.cmcc.xml.content.QryuserinfoContent;

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
