package org.com.coolfish.common.webinterface.cmcc.xml.in;

import org.com.coolfish.common.webinterface.cmcc.xml.content.QrycycleContent;

/*
 * 物联网单卡实时流量查询 报文
 */
public class QrycycleOperationIn extends OperationIn {
	private QrycycleContent content;
	public QrycycleContent getContent() {
		return content;
	}
	public void setContent(QrycycleContent content) {
		this.content = content;
	}

}
