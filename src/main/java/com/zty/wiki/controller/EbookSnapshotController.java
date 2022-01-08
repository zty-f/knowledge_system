/**
 * @author: zty
 * @program: knowledge_system
 * @ClassName EbookSnapshotController
 * @description:
 * @create: 2022-01-08 17:00
 * @Version 1.0
 **/
package com.zty.wiki.controller;


import com.zty.wiki.resp.CommonResp;
import com.zty.wiki.resp.StatisticResp;
import com.zty.wiki.service.EbookSnapshotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebookSnapshot")
public class EbookSnapshotController {
    @Resource
    private EbookSnapshotService ebookSnapshotService;

    @GetMapping("/getStatistic")
    public CommonResp getStatistic(){
        List<StatisticResp> statisticResp = ebookSnapshotService.getStatistic();
        CommonResp<List<StatisticResp>> commonResp = new CommonResp<>();
        commonResp.setContent(statisticResp);
        return commonResp;
    }
    @GetMapping("/get30Statistic")
    public CommonResp get30Statistic(){
        List<StatisticResp> statisticResp = ebookSnapshotService.get30Statistic();
        CommonResp<List<StatisticResp>> commonResp = new CommonResp<>();
        commonResp.setContent(statisticResp);
        return commonResp;
    }

}
