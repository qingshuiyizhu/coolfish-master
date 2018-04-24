package org.com.coolfish.cmcc.service;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.stereotype.Service;
@Service
public class Pool {
	 // 连接池对象
	private PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
	private CloseableHttpClient httpclient;
	{
	// 将最大连接数增加到200
	connectionManager.setMaxTotal(200);
	// 将每个路由的默认最大连接数增加到20
	connectionManager.setDefaultMaxPerRoute(20); 
	// HttpClient对象
	 httpclient = HttpClients.custom().setConnectionManager(connectionManager).build();
	}
	public CloseableHttpClient getHttpclient() {
		return httpclient;
	}
	public void setHttpclient(CloseableHttpClient httpclient) {
		this.httpclient = httpclient;
	}
	
}
