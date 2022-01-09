package com.zty.wiki.schedule;

import com.zty.wiki.service.DocService;
import com.zty.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * springboot定时任务是单线程的！！！！！
 */
@Component
public class DocJob {

    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);
    @Resource
    private DocService docService;
    @Resource
    private SnowFlake snowFlake;

    /**
     * 每隔2分钟执行一次电子书信息更新
     */
    @Scheduled(cron = "0 0/2 * * * ? ")
    public void cron() {
        //增加日志流水号
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("更新电子书相关数据开始·····");
        long start = System.currentTimeMillis();
        docService.updateEbookInfo();
        LOG.info("更新电子书相关数据结束,总共耗时:{}毫秒", System.currentTimeMillis() - start);
    }

}
