package org.com.coolfish.cmcc;

import org.com.coolfish.cmcc.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication = (默认属性)@Configuration + @EnableAutoConfiguration + @ComponentScan。
@SpringBootApplication
public class Application implements CommandLineRunner {
	@Autowired
	private MainService mainService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Thread.currentThread().join();
		mainService.start();
	}
}
