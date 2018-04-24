package org.com.coolfish.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
/**
 * 线程池任务
 * @author mingge
 *
 */
@Service
public class AsyncTaskService0 {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
  
   @Async
	public void executeAsyncTask(String i){
  
	}
 
	@Async
	public void executeAsyncTask() {
 	}
	 
 
	}
