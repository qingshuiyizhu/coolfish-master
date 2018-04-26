package org.com.coolfish.common.webinterface.cmcc.xml.in;

import org.com.coolfish.common.webinterface.cmcc.xml.content.SuspendtestContent;

/*
 * 物联网单卡实时流量查询 报文
 */
public class SuspendtestOperationIn extends OperationIn {
	private SuspendtestContent content;
	public SuspendtestContent getContent() {
		return content;
	}
	public void setContent(SuspendtestContent content) {
		this.content = content;
	}

}
