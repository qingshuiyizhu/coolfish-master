package org.com.coolfish;

import org.com.coolfish.common.database.service.ComDBService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/*//这是JUnit的注解，通过这个注解让SpringJUnit4ClassRunner这个类提供Spring测试上下文。  
@RunWith(SpringJUnit4ClassRunner.class)
// 这是Spring Boot注解，为了进行集成测试，需要通过这个注解加载和配置Spring应用上下
@SpringApplicationConfiguration(classes = SpringJUnitTestApplication.class)*/
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = Application.class)
public class Test {
    @Autowired
    private ComDBService databaseService;

    @org.junit.Test
    public void Test1() {
        System.out.println(databaseService);
         databaseService.flashSlientStatus("1064619116678");
    }
}
