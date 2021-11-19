/**
 * @author: zty
 * @program: knowledge_system
 * @ClassName TestController
 * @description:
 * @create: 2021-11-19 10:14
 * @Version 1.0
 **/
package com.zty.knowledge_system.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    /*@GetMapping()
    @PutMapping()
    @DeleteMapping()
    @PostMapping()
    Restful风格
    @RequestMapping(value = "/hello/1",method = RequestMethod.DELETE)*/
    @RequestMapping("/hello")
    // GET POST DELETE  PUT
    public String hello(){
        return "Hello World!";
    }

    @PostMapping("/hello/post")
    // GET POST DELETE  PUT
    public String hello(String name){
        return "Hello World!"+name;
    }
}
