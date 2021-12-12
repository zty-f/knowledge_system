/**
 * @author: zty
 * @program: wiki
 * @ClassName TestController
 * @description:
 * @create: 2021-11-19 10:14
 * @Version 1.0
 **/
package com.zty.wiki.controller;

import com.zty.wiki.domain.Test;
import com.zty.wiki.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class TestController {

    private static final Logger LOG  = LoggerFactory.getLogger(TestController.class);
    @Resource
    private TestService testService;

    @Resource
    private RedisTemplate redisTemplate;
    @Value("${test.hello:TEST}") // :TEST 设置配置默认值，配置文件没有配置，即为默认值，配置文件有配置则优先配置文件
    private String testhello; //自定义配置文件实现
    /*@GetMapping()
    @PutMapping()
    @DeleteMapping()
    @PostMapping()
    Restful风格
    @RequestMapping(value = "/hello/1",method = RequestMethod.DELETE)*/
    @RequestMapping("/hello")
    // GET POST DELETE  PUT
    public String hello(){
        return "Hello World!"+testhello;
    }

    @PostMapping("/hello/post")
    // GET POST DELETE  PUT
    public String hello(String name){
        return "Hello World!"+name;
    }

    @GetMapping("/test/list")
    public List<Test> list(){
        return testService.list();
    }

    @RequestMapping("/redis/set/{key}/{value}")
    public String set(@PathVariable String key, @PathVariable String value) {
        redisTemplate.opsForValue().set(key, value, 3600, TimeUnit.SECONDS);
        LOG.info("key: {}, value: {}", key, value);
        return "success";
    }

    @RequestMapping("/redis/get/{key}")
//    public Object get(@PathVariable Long key) {
    public Object get(@PathVariable String key) {
        Object object = redisTemplate.opsForValue().get(key);
        LOG.info("key: {}, value: {}", key, object);
        return object;
    }

}
