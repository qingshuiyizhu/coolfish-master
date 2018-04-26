package org.com.coolfish.common.webinterface.cmcc.xml.in;

import org.com.coolfish.common.webinterface.cmcc.xml.content.TFJContent;

/*
 * 物联网集团成员停复机 报文
 */
public class TFJOperationIn extends OperationIn {
	private TFJContent content;
	public TFJContent getContent() {
		return content;
	}
	public void setContent(TFJContent content) {
		this.content = content;
	}

}
