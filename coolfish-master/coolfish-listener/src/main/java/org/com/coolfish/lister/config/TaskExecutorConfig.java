package org.com.coolfish.lister.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
// @ComponentScan("org.com.dev.socket")
@EnableAsync //开启异步任务
@EnableScheduling // 开启定时任务
public class TaskExecutorConfig implements AsyncConfigurer {
	private String ThreadNamePrefix = "Executor-";
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		// 线程池维护线程的最少数量
		taskExecutor.setCorePoolSize(10);
		// 线程池维护线程的最大数量
		taskExecutor.setMaxPoolSize(20);
		// 线程池所使用的缓冲队列
		taskExecutor.setQueueCapacity(20);
		// 线程池维护线程所允许的空闲时间 超出这个时间将进行回收  
		taskExecutor.setKeepAliveSeconds(30000);
		taskExecutor.setThreadNamePrefix(ThreadNamePrefix);
	  	// rejection-policy：当pool已经达到max size的时候，怎样处理新任务
	 	//ABORT（缺省）：抛出TaskRejectedException异常，然后不运行
		//DISCARD：不运行，也不抛出异常
		//DISCARD_OLDEST：丢弃queue中最旧的那个任务
		//CALLER_RUNS：不在新线程中运行任务，而是有调用者所在的线程来运行
		 
		taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		//等待线程全部执行完关闭线程池
		//taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		
		
	/*	  // 设置拒绝策略
		taskExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
	      @Override
	      public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
	        // .....
	      }
	    });*/
	    // 使用预定义的异常处理类
	    // executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
 	
		taskExecutor.initialize();
		return taskExecutor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return null;
	}
 	
}