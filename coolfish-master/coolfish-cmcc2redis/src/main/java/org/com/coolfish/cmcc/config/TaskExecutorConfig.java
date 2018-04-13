package org.com.coolfish.cmcc.config;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
//@ComponentScan("org.com.dev.socket")
@EnableAsync
@EnableScheduling //开启定时任务
public class TaskExecutorConfig implements AsyncConfigurer{

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
      //线程池维护线程的最少数量 
        taskExecutor.setCorePoolSize(10);
        //线程池维护线程的最大数量  
        taskExecutor.setMaxPoolSize(20);
        //线程池所使用的缓冲队列  
        taskExecutor.setQueueCapacity(500);
        //线程池维护线程所允许的空闲时间  
        taskExecutor.setKeepAliveSeconds(30000);  
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}