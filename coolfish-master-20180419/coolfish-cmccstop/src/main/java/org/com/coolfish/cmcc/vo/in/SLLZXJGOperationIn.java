package org.com.coolfish.cmcc.vo.in;

import org.com.coolfish.cmcc.vo.in.content.SLLZXJGContent;


/*
 * 受理类执行结果查询 报文
 */
public class SLLZXJGOperationIn extends OperationIn {
	private SLLZXJGContent content;
	public SLLZXJGContent getContent() {
		return content;
	}
	public void setContent(SLLZXJGContent content) {
		this.content = content;
	}

}
