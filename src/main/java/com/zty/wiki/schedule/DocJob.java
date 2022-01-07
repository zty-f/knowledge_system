package com.zty.wiki.schedule;
 import com.zty.wiki.service.DocService;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.scheduling.annotation.Scheduled;
 import org.springframework.stereotype.Component;

 import javax.annotation.Resource;
 import java.text.SimpleDateFormat;
 import java.util.Date;

/**
 * springboot定时任务是单线程的！！！！！
 */
@Component
 public class DocJob {

    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);
    @Resource
    private DocService docService;

    /**
     * 每隔30秒钟执行一次电子书信息更新
     */
    @Scheduled(cron = "10/30 * * * * ?")
    public void cron(){
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss SSS");
        String dateString = formatter.format(new Date());
        docService.updateEbookInfo();
        LOG.info("每隔30秒钟执行一次电子书信息更新： {}", dateString);
    }

 }
