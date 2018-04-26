package org.com.coolfish.common.webinterface.cmcc.xml.in;

import org.com.coolfish.common.webinterface.cmcc.xml.content.QrystatusContent;

/*
 *  用户状态查询 报文
 */
public class QrystatusOperationIn extends OperationIn {
	private QrystatusContent content;
	public QrystatusContent getContent() {
		return content;
	}
	public void setContent(QrystatusContent content) {
		this.content = content;
	}

}
