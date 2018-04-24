package org.com.coolfish.cmcc.vo.out;

public class prodinfo {
	//产品编码
	private String product_code;
	//产品名称
	private String product_name;
	//物联网卡套餐剩余量
	private Long left_value;
	//物联网卡套餐使用量
	private Long cumulate_value;

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public Long getLeft_value() {
		return left_value;
	}

	public void setLeft_value(Long left_value) {
		this.left_value = left_value;
	}

	public Long getCumulate_value() {
		return cumulate_value;
	}

	public void setCumulate_value(Long cumulate_value) {
		this.cumulate_value = cumulate_value;
	}

}
