package org.com.coolfish.cmcc.service;

import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

public class XMLHttpPost {
	// 使用POST方法发送XML数据
	@SuppressWarnings("deprecation")
	public String sendXMLHttpPost(String url, String xmlData) throws Exception {
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		StringEntity myEntity = new StringEntity(xmlData);
		httppost.addHeader("Content-Type", "text/xml");
		httppost.setEntity(myEntity);
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
