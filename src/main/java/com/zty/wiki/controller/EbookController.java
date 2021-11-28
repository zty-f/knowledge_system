/**
 * @author: zty
 * @program: wiki
 * @ClassName TestController
 * @description:
 * @create: 2021-11-19 10:14
 * @Version 1.0
 **/
package com.zty.wiki.controller;

import com.zty.wiki.req.EbookQueryReq;
import com.zty.wiki.req.EbookSaveReq;
import com.zty.wiki.resp.CommonResp;
import com.zty.wiki.resp.EbookQueryResp;
import com.zty.wiki.resp.PageResp;
import com.zty.wiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/all")
    public CommonResp all(){
        CommonResp<List<EbookQueryResp>> resp = new CommonResp<>();
        List<EbookQueryResp> list= ebookService.all();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list")
    public CommonResp list(EbookQueryReq req){
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list= ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq req){
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long  id){
        CommonResp resp = new CommonResp<>();
        ebookService.delete(id);
        return resp;
    }

}
