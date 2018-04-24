package org.com.coolfish.cmcc.vo.out;

public class poolinfo {
	// 集团流量池剩余量
	private Long pool_left_value;
	// 物联网卡使用量
	private Long cumulate_value;

	public Long getPool_left_value() {
		return pool_left_value;
	}

	public void setPool_left_value(Long pool_left_value) {
		this.pool_left_value = pool_left_value;
	}

	public Long getCumulate_value() {
		return cumulate_value;
	}

	public void setCumulate_value(Long cumulate_value) {
		this.cumulate_value = cumulate_value;
	}

}
