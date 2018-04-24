package org.com.coolfish.cmcc.vo.out;

import org.com.coolfish.cmcc.vo.out.content.DKLLCXContent;

/*
 * 物联网动态流量池实时情况查询 应答报文
 */
public class DKLLCXOperationOut extends OperationOut {

	private DKLLCXContent content;

	public DKLLCXContent getContent() {
		return content;
	}

	public void setContent(DKLLCXContent content) {
		this.content = content;
	}

}
