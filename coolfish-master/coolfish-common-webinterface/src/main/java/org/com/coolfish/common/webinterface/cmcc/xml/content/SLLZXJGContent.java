package org.com.coolfish.common.webinterface.cmcc.xml.content;

import org.springframework.boot.autoconfigure.web.ResourceProperties.Content;

@SuppressWarnings("unused")
public class SLLZXJGContent extends Content {
	// 地市编码
	private String ddr_city="";
	// 订单编号
	private String orderid="";

	public void setDdr_city(String ddr_city) {
		this.ddr_city = ddr_city;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public SLLZXJGContent(String ddr_city, String orderid) {
		super();
		this.ddr_city = ddr_city;
		this.orderid = orderid;
	}

	public SLLZXJGContent() {
		super();

	}

}
