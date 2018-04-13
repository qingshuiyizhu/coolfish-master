package org.com.coolfish.cmcc.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.com.coolfish.cmcc.vo.in.DKLLCXOperationIn;
import org.com.coolfish.cmcc.vo.in.QrystatusOperationIn;
import org.com.coolfish.cmcc.vo.in.content.DKLLCXContent;
import org.com.coolfish.cmcc.vo.in.content.QrystatusContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

public class PostThread extends Thread {
	private final CloseableHttpClient httpClient;
	private final String tel;
	private final String url;
	private final String cycle;
	private final String req_seq;
	private final String appid;
	private final String token;
	private final String groupid;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public PostThread(CloseableHttpClient httpClient, String tel, String cycle, String req_seq, String url,
			String appid, String token, String groupid) {
		super();
		this.httpClient = httpClient;
		this.tel = tel;
		this.url = url;
		this.cycle = cycle;
		this.req_seq = req_seq;
		this.appid = appid;
		this.token = token;
		this.groupid = groupid;
	}

	@Override
	public void run() {
		long startTime, endTime;
		startTime = System.currentTimeMillis(); // 获取开始时间
		String xml = executeAsyncDKLLCXQuery();
		String xml2 = executeAsyncQrystatusQuery();
		System.out.println(xml + xml2);
	}

	private String executeAsyncQrystatusQuery() {
		long startTime, endTime;
		startTime = System.currentTimeMillis(); // 获取开始时间

		HttpEntity resEntity = null;
		try {
			HttpPost httppost = new HttpPost(url);
			httppost.addHeader("Content-Type", "text/xml");
			String XML_HEADER = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n";
			// QrystatusQuery
			String desc = "用户状态查询:" + "telnum:号码 " + tel + ",req_seq:流水号" + req_seq;
			// 报文
			QrystatusOperationIn operationIn = new QrystatusOperationIn();
			// 业务功能代码

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String req_time = sdf.format(new Date());
			operationIn.setProcess_code("cc_wlw_qrystatus");
			operationIn.setReq_time(req_time);
			operationIn.setApp_id(appid);
			operationIn.setAccess_token(token);
			// 本次调用请求流水号
			operationIn.setReq_seq(req_seq);
			// 请求类型
			operationIn.setReq_type("1");
			operationIn.setContent(new QrystatusContent(groupid, tel));
			// 创建xStream对象
			XStream xStream = new XStream();
			// 设置别名, 默认会输出全路径
			xStream.alias("operation_in", QrystatusOperationIn.class);
			// 调用toXML 将对象转成字符串
			String s = xStream.toXML(operationIn);
			s = s.replace("__", "_");
			StringBuffer xml = new StringBuffer();
			xml.append(XML_HEADER);
			xml.append(s);
			StringEntity myEntity = new StringEntity(xml.toString());
			httppost.setEntity(myEntity);
			HttpResponse response = httpClient.execute(httppost);
			resEntity = response.getEntity();
			String str = EntityUtils.toString(resEntity, Consts.UTF_8);
			endTime = System.currentTimeMillis(); // 获取结束时间
			// logger.info(desc + "返回xml数据为:\n" + xml+"消耗时间：" + (endTime - startTime) +
			// "ms");
			return req_seq+str;
		} catch (Exception e) {
		} finally {
			EntityUtils.consumeQuietly(resEntity);// 释放连接
		}
		return null;

	}

	public String executeAsyncDKLLCXQuery() {
		long startTime, endTime;
		startTime = System.currentTimeMillis(); // 获取开始时间
		HttpEntity resEntity = null;
		try {
			HttpPost httppost = new HttpPost(url);
			httppost.addHeader("Content-Type", "text/xml");
			String XML_HEADER = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n";
			// DKLLCXQuery
			String desc = "物联网单卡实时流量查询:" + "查询号码为：" + tel + ",查询月数为:" + cycle + ",req_seq:请求流水号:" + req_seq;
			// 报文
			DKLLCXOperationIn operationIn = new DKLLCXOperationIn();
			// 业务功能代码
			operationIn.setProcess_code("OPEN_QRYINTERNETUSERGPRS");
			operationIn.setContent(new DKLLCXContent(tel, cycle));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String req_time = sdf.format(new Date());
			operationIn.setReq_time(req_time);
			// 本次调用请求流水号0_9834345
			operationIn.setReq_seq(req_seq);
			operationIn.setApp_id(appid);
			operationIn.setAccess_token(token);
			// 请求类型
			operationIn.setReq_type("1");

			// 创建xStream对象
			XStream xStream = new XStream();
			// 设置别名, 默认会输出全路径
			xStream.alias("operation_in", DKLLCXOperationIn.class);
			// 调用toXML 将对象转成字符串
			String s = xStream.toXML(operationIn);
			s = s.replace("__", "_");
			StringBuffer xml = new StringBuffer();
			xml.append(XML_HEADER);
			xml.append(s);
			StringEntity myEntity = new StringEntity(xml.toString());
			httppost.setEntity(myEntity);
			HttpResponse response = httpClient.execute(httppost);
			resEntity = response.getEntity();
			String str = EntityUtils.toString(resEntity, Consts.UTF_8);
			endTime = System.currentTimeMillis(); // 获取结束时间
			// logger.info(desc + "返回xml数据为:\n" + xml+"消耗时间：" + (endTime - startTime) +
			// "ms");
			return req_seq+str;
		} catch (Exception e) {
		} finally {
			EntityUtils.consumeQuietly(resEntity);// 释放连接
		}
		return null;
	}
}
