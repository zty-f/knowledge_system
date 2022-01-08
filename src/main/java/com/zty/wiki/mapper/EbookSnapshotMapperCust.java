/**
 * @author: zty
 * @program: knowledge_system
 * @description:
 * @create: 2022-01-08 16:45
 * @Version 1.0
 **/
package com.zty.wiki.mapper;

import com.zty.wiki.resp.StatisticResp;

import java.util.List;

public interface EbookSnapshotMapperCust {

     void generateSnapshot();

     List<StatisticResp> getStatistic();

     List<StatisticResp> get30Statistic();

}
