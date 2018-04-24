package org.com.coolfish.cmcc.vo.out.content;

import java.util.List;

import org.com.coolfish.cmcc.vo.out.gprsinfo;

/*
 * 物联网动态流量池实时情况查询 应答参数
 */
public class DTLLCContent {
	private List<gprsinfo> resultlist;

	public List<gprsinfo> getResultlist() {
		return resultlist;
	}

	public void setResultlist(List<gprsinfo> resultlist) {
		this.resultlist = resultlist;
	}

}
