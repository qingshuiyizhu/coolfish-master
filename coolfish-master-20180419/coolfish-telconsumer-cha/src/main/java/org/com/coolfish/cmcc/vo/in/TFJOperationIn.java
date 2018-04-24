package org.com.coolfish.cmcc.vo.in;

import org.com.coolfish.cmcc.vo.in.content.TFJContent;

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
