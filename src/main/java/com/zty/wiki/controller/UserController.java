/**
 * @author: zty
 * @program: wiki
 * @ClassName TestController
 * @description:
 * @create: 2021-11-19 10:14
 * @Version 1.0
 **/
package com.zty.wiki.controller;

import com.zty.wiki.req.UserQueryReq;
import com.zty.wiki.req.UserSaveReq;
import com.zty.wiki.resp.CommonResp;
import com.zty.wiki.resp.UserQueryResp;
import com.zty.wiki.resp.PageResp;
import com.zty.wiki.service.UserService;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/all")
    public CommonResp all(){
        CommonResp<List<UserQueryResp>> resp = new CommonResp<>();
        List<UserQueryResp> list= userService.all();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq req){
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list= userService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long  id){
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }

}
