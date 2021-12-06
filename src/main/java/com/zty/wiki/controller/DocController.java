/**
 * @author: zty
 * @program: wiki
 * @ClassName TestController
 * @description:
 * @create: 2021-11-19 10:14
 * @Version 1.0
 **/
package com.zty.wiki.controller;

import com.zty.wiki.req.DocQueryReq;
import com.zty.wiki.req.DocSaveReq;
import com.zty.wiki.resp.DocQueryResp;
import com.zty.wiki.resp.CommonResp;
import com.zty.wiki.resp.PageResp;
import com.zty.wiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Resource
    private DocService docService;

    @GetMapping("/all")
    public CommonResp all(){
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list= docService.all();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq req){
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list= docService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req){
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{idsStr}")
    public CommonResp delete(@PathVariable String  idsStr){
        CommonResp resp = new CommonResp<>();
        List<String> ids =  Arrays.asList(idsStr.split(","));
        docService.delete(ids);
        return resp;
    }

}
