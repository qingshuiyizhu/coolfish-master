package org.com.coolfish.cmcc.vo.out.content;

import java.util.List;

import org.com.coolfish.cmcc.vo.out.poolinfo;
import org.com.coolfish.cmcc.vo.out.prodinfo;

public class DKLLCXContent {
  
	private List<poolinfo> poollist; 
	private List<prodinfo> prodlist;
	// 标准资费流量
	private Long out_gprsflus;

	  public List<poolinfo> getPoollist() {
		return poollist;
	}

	public void setPoollist(List<poolinfo> poollist) {
		this.poollist = poollist;
	} 
 
	public List<prodinfo> getProdlist() {
		return prodlist;
	}

	public void setProdlist(List<prodinfo> prodlist) {
		this.prodlist = prodlist;
	}

	public Long getOut_gprsflus() {
		return out_gprsflus;
	}

	public void setOut_gprsflus(Long out_gprsflus) {
		this.out_gprsflus = out_gprsflus;
	}

}
