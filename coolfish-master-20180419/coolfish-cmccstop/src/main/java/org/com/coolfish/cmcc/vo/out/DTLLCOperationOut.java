package org.com.coolfish.cmcc.vo.out;

import org.com.coolfish.cmcc.vo.out.content.DTLLCContent;

/*
 * 物联网动态流量池实时情况查询 应答报文
 */
public class DTLLCOperationOut extends OperationOut {
	private DTLLCContent content;

	public DTLLCContent getContent() {
		return content;
	}

	public void setContent(DTLLCContent content) {
		this.content = content;
	}
	
}
