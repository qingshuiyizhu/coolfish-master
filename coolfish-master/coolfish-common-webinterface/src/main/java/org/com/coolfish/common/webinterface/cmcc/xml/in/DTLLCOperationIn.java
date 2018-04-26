package org.com.coolfish.common.webinterface.cmcc.xml.in;

import org.com.coolfish.common.webinterface.cmcc.xml.content.DTLLCContent;

/*
 * 物联网动态流量池实时情况查询 报文
 */
public class DTLLCOperationIn extends OperationIn {
	private DTLLCContent content;
	public DTLLCContent getContent() {
		return content;
	}
	public void setContent(DTLLCContent content) {
		this.content = content;
	}

}
