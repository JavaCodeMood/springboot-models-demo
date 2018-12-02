package com.lhf.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: HelloController
 * @Description:  http://localhost:8080/index/hello
 * @Author: liuhefei
 * @Date: 2018/12/2
 **/
@RestController
@RequestMapping("/index")
public class HelloController {

    @RequestMapping(value="/hello")
    public String helloWorld(){
       return "Hello World!";
    }
}
