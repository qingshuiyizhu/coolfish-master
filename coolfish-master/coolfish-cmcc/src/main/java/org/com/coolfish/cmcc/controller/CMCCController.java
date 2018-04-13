package org.com.coolfish.cmcc.controller;

import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.com.coolfish.cmcc.service.CMCCService;
import org.com.coolfish.cmcc.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/cmcc")
public class CMCCController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private CMCCService service;
	
	 
	@RequestMapping(value = "/test")
	@ResponseBody
	public String test() {
		return service.DKLLCXQuery("17892088908", 201803, "0_9834345");
	 }
	
	
	
	@RequestMapping(value = "/t")
	@ResponseBody
	public String test1() {
		 //http://183.230.96.66:8087/v2/groupuserinfo?appid=xxx&ebid=xxx&transid=xxx&token=xxx&query_date=xxx
		    JSONObject params = new JSONObject();
	        params.put("appid", "name");
	        params.put("ebid", "230882xxxxxx2116");
	      JSONObject param2 = new JSONObject();
	        param2.put("pageNo", 1);
	        param2.put("pageSize", 20);
	        params.put("page", param2);
	        String param = "q="+params.toString();
	        
	        System.out.println(param);
	        new HttpUtil(); 
			//get 请求
	        String ret = HttpUtil.getSerchPersion("http://183.230.96.66:8087/v2/groupuserinfo", param.toString());
		
		
		
		return "0";
	 }
	
	
	 
	/* public static JSONObject get(String url) {  
         
	        HttpClient client = new DefaultHttpClient();  
	        HttpGet get = new HttpGet(url);  
	        JSONObject json = null;  
	        try {  
	            HttpResponse res = client.execute(get);  
	            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
	                HttpEntity entity = res.getEntity();  
	                json = new JSONObject(new org.json.JSONTokener(new InputStreamReader(entity.getContent(), HTTP.UTF_8)));  
	            }  
	        } catch (Exception e) {  
	            throw new RuntimeException(e);  
	              
	        } finally{  
	            //关闭连接 ,释放资源  
	            client.getConnectionManager().shutdown();  
	        }  
	        return json;  
	    }   
	*/
	
	public static String sendHttpPost(String url, String body) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.setEntity(new StringEntity(body));

		CloseableHttpResponse response = httpClient.execute(httpPost);
		System.out.println(response.getStatusLine().getStatusCode() + "\n");
		HttpEntity entity = response.getEntity();
		String responseContent = EntityUtils.toString(entity, "UTF-8"); 
		System.out.println(responseContent);

		response.close();
		httpClient.close();
		return responseContent;
		}
	
}
