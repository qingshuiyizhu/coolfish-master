package org.com.coolfish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 部署到外部的Tomcat容器中：修改启动类，继承 SpringBootServletInitializer 并重写 configure 方法
 * 
 * @author
 *
 */
public class SpringBootStartApplication extends SpringBootServletInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SpringBootStartApplication.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        logger.info("以外部的Tomcat容器启动本项目!");
        return builder.sources(Application.class);
    }

}