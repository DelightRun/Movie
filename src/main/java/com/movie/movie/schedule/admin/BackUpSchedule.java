package com.movie.movie.schedule.admin;

import com.movie.movie.service.admin.DatabaseBakService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 备份数据库定时器
 *
 * @author Administrator
 */
@Configuration
@EnableScheduling
public class BackUpSchedule {

    @Autowired
    private DatabaseBakService databaseBakService;

    private final Logger log = LoggerFactory.getLogger(BackUpSchedule.class);

    //@Scheduled(initialDelay=10000,fixedRate=5000)
    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨一点0分0秒执行备份任务
    public void backUpDatabase() {
        log.info("开始执行定时备份数据库任务！");
        databaseBakService.backup();
    }


}
