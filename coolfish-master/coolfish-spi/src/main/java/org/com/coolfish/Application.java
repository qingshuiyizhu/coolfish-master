
package org.com.coolfish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//@EnableEurekaClient //注解是基于spring-cloud-netflix依赖，只能eureka使用
@EnableDiscoveryClient //注解是基于spring-cloud-commons依赖，并且在classpath中实现
@SpringBootApplication
public class Application {
     public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    
    }

}
