package org.com.coolfish.common.spi.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("test")
public class TestController {
    @RequestMapping(value = "test/{id}", method = RequestMethod.GET)
    public String test(@PathVariable Integer id) {
       log.info("请求成功---Success"+id); 
        return "Success";
    }
}
