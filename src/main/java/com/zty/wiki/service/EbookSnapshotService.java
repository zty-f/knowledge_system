/**
 * @author: zty
 * @program: knowledge_system
 * @ClassName EbookSnapshotService
 * @description:
 * @create: 2022-01-08 16:46
 * @Version 1.0
 **/
package com.zty.wiki.service;

import com.zty.wiki.mapper.EbookSnapshotMapperCust;
import com.zty.wiki.resp.StatisticResp;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookSnapshotService {
    @Resource
    private EbookSnapshotMapperCust ebookSnapshotMapperCust;

    public void generateSnapshot(){
        ebookSnapshotMapperCust.generateSnapshot();
    }

    public List<StatisticResp> getStatistic(){
        return ebookSnapshotMapperCust.getStatistic();
    }
    public List<StatisticResp> get30Statistic(){
        return ebookSnapshotMapperCust.get30Statistic();
    }
}
