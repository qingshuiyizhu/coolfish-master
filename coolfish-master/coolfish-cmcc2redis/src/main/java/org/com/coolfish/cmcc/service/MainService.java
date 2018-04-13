package org.com.coolfish.cmcc.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.com.coolfish.cmcc.config.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainService {
	@Autowired
	private RedisService redisService;
	@Autowired
	private Param param;
	
  	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void start() {
      	// 连接池对象
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		// 将最大连接数增加到200
		connectionManager.setMaxTotal(200);
		// 将每个路由的默认最大连接数增加到20
		connectionManager.setDefaultMaxPerRoute(20);
		// HttpClient对象
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
		ExecutorService pool = Executors.newFixedThreadPool(param.getThreadNumber());
		Thread thread = null;
		System.out.println("----------------------------------------程序开始-----------------------------------------");
		 final long start = System.currentTimeMillis(); // 获取开始时间
		List<String> list = redisService.getTelAll();
	 	for (int i = 0; i <10000; i++) {
	   	  thread = new PostThread(httpClient,list.get(i), "201803", "0_"+(i+1),param.getUrl(),param.getAppid(),param.getToken(),param.getGroupid());
	   	  thread.start();
	      pool.execute(thread);
	     if(i % 500 == 0) {
	    	  try {
				Thread.sleep(1000);
		 	} catch (InterruptedException e) {  
				e.printStackTrace();
			} 
	      }
 	}
	 	
	 	pool.shutdown();
	 	 while(true){  
	           if(pool.isTerminated()){  
	                 System.out.println("所有的子线程都结束了！");  
	                break;  
	            }  
	            try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				 
					e.printStackTrace();
				}    
	        }
	        final long end = System.currentTimeMillis();// 获取结束时间
	    	logger.info("\n查詢号码个数为：" + 10000); // 输出程序运行时间
			logger.info("\n程序运行时间：" + (end - start) + "ms"); // 输出程序运行时间
			System.out.println("----------------------------程序退出--------------------------------");
			System.exit(0);
	    }
	 
     	
	}

	 
 
