package org.com.coolfish.cmcc.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.com.coolfish.cmcc.vo.in.DKLLCXOperationIn;
import org.com.coolfish.cmcc.vo.in.QrystatusOperationIn;
import org.com.coolfish.cmcc.vo.in.content.DKLLCXContent;
import org.com.coolfish.cmcc.vo.in.content.QrystatusContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;
/**
 * 线程池任务
 * @author mingge
 *
 */
@Service
public class AsyncTaskService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
   @Async
   public void  executeAsyncDKLLCXQuery(CloseableHttpClient httpClient, String tel, String cycle,String req_seq,String url,String appid,String token,String groupid){
	  
	    long startTime,endTime;
		startTime = System.currentTimeMillis();    //获取开始时间
		HttpEntity resEntity = null;
		try {
			HttpClient httpclient1 = HttpClients.createDefault();
		 	HttpPost httppost = new HttpPost(url);
		    httppost.addHeader("Content-Type", "text/xml");
		    String XML_HEADER = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n";
		    //DKLLCXQuery
		    String desc = "物联网单卡实时流量查询:" + "查询号码为：" + tel + ",查询月数为:" + cycle + ",req_seq:请求流水号:" + req_seq;
			logger.info(desc);
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
			//logger.info(desc + "发送xml数据为:\n" + xml);
		     StringEntity myEntity = new StringEntity(xml.toString());
			httppost.setEntity(myEntity);
		
		 	HttpResponse response = httpClient.execute(httppost);
			resEntity = response.getEntity();
		 	String str = EntityUtils.toString(resEntity, Consts.UTF_8);
		 	System.out.println(req_seq+"====DKLLCXQuery========" + str);
		 	//QrystatusQuery
		  	 desc = "用户状态查询:" + "telnum:号码 " + tel + ",req_seq:流水号" + req_seq;
			//logger.info(desc);
			// 报文
			QrystatusOperationIn operationIn1 = new QrystatusOperationIn();
			// 业务功能代码
	        	req_time = sdf.format(new Date());
			operationIn1.setProcess_code("cc_wlw_qrystatus");
			operationIn1.setReq_time(req_time);
			operationIn1.setApp_id(appid);
			operationIn1.setAccess_token(token);
			// 本次调用请求流水号
			operationIn1.setReq_seq(req_seq);
			// 请求类型
			operationIn1.setReq_type("1");
			operationIn1.setContent(new QrystatusContent(groupid, tel));
	 		// 创建xStream对象
		        xStream = new XStream();
			// 设置别名, 默认会输出全路径
			xStream.alias("operation_in", QrystatusOperationIn.class);
			// 调用toXML 将对象转成字符串
			 s = xStream.toXML(operationIn);
			 s = s.replace("__", "_");
			xml = new StringBuffer();
			xml.append(XML_HEADER);
			xml.append(s);
			//logger.info(desc + "发送xml数据为:\n" + xml);
		        myEntity = new StringEntity(xml.toString());
			httppost.setEntity(myEntity);
			response = httpClient.execute(httppost);
			resEntity = response.getEntity();
		    str = EntityUtils.toString(resEntity, Consts.UTF_8);
		 	
		    System.out.println(req_seq+"====QrystatusQuery========" + str); 
		   //System.out.println("client object:" + httpClient);
			endTime = System.currentTimeMillis();    //获取结束时间
		  //  logger.info("\n第"+req_seq+"次查询消耗时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
	 } catch (Exception e) {
        	} finally {
			EntityUtils.consumeQuietly(resEntity);// 释放连接
		}
	 
	  
	}
   @Async
	public void executeAsyncTask(String tel,String i){
  
	}
 
	@Async
	public void executeAsyncTask() {
 	}
	 
 
	}
