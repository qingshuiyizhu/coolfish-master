package org.com.coolfish.cmcc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Param {
	@Value("${coolfish.cmcc.url}")
	private String url;
	@Value("${coolfish.cmcc.appid}")
	private String appid;
	@Value("${coolfish.cmcc.token}")
	private String token;
	@Value("${coolfish.cmcc.groupid}")
	private String groupid;
	@Value("${coolfish.cmcc.password}")
	private String password;
	@Value("${coolfish.cmcc.ddrcity}")
	private String ddrcity;
	@Value("${coolfish.cmcc.appid2}")
	private String appid2;

	public String getUrl() {
		return url;
	}

	public String getAppid() {
		return appid;
	}

	public String getToken() {
		return token;
	}

	public String getGroupid() {
		return groupid;
	}

	public String getPassword() {
		return password;
	}

	public String getDdrcity() {
		return ddrcity;
	}

	public String getAppid2() {
		return appid2;
	}

}
