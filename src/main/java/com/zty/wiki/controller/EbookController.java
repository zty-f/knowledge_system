/**
 * @author: zty
 * @program: wiki
 * @ClassName TestController
 * @description:
 * @create: 2021-11-19 10:14
 * @Version 1.0
 **/
package com.zty.wiki.controller;

import com.zty.wiki.req.EbookReq;
import com.zty.wiki.resp.CommonResp;
import com.zty.wiki.resp.EbookResp;
import com.zty.wiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(EbookReq req){
        CommonResp<List<EbookResp>> resp = new CommonResp<>();
        List<EbookResp> list= ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

}
