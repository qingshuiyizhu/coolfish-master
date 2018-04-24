package org.com.coolfish.cmcc.service;

import java.io.InputStreamReader;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XMLHttpPost {
	@Autowired
	private Pool pool;
 	
	
	
	@SuppressWarnings("deprecation")
	public String sendXMLHttpPost(String url, String xmlData) throws Exception {
	System.out.println("pool----"+pool);
		CloseableHttpClient httpclient = pool.getHttpclient();
		// HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		StringEntity myEntity = new StringEntity(xmlData);
		httppost.addHeader("Content-Type", "text/xml");
		httppost.setEntity(myEntity);
		//RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();// 设置请求和传输超时时间
		//httppost.setConfig(requestConfig);
		HttpResponse response = httpclient.execute(httppost);
		 System.out.println("client object:"+httpclient);  
		 HttpEntity resEntity = response.getEntity();
		 String str= EntityUtils.toString(resEntity, Consts.UTF_8);
        //System.out.println("============"+EntityUtils.toString(resEntity, Consts.UTF_8)+"=============");  
         EntityUtils.consumeQuietly(resEntity);// 释放连接  
	/*	HttpEntity resEntity = response.getEntity();
		InputStreamReader reader = new InputStreamReader(resEntity.getContent(), "GBK");
		char[] buff = new char[1024];
		int length = 0;
		StringBuffer result = new StringBuffer();
		while ((length = reader.read(buff)) != -1) {
			result.append(new String(buff, 0, length));
		}
		httpclient.getConnectionManager().shutdown();
*/
		return str;
	}

	public String sendXMLHttpPost1(String url, String xmlData) throws Exception {
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		StringEntity myEntity = new StringEntity(xmlData);
		httppost.addHeader("Content-Type", "text/xml");
		httppost.setEntity(myEntity);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();// 设置请求和传输超时时间
		httppost.setConfig(requestConfig);
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity resEntity = response.getEntity();
		InputStreamReader reader = new InputStreamReader(resEntity.getContent(), "GBK");
		char[] buff = new char[1024];
		int length = 0;
		StringBuffer result = new StringBuffer();
		while ((length = reader.read(buff)) != -1) {
			result.append(new String(buff, 0, length));
		}
		httpclient.getConnectionManager().shutdown();

		return result.toString();
	}

}
