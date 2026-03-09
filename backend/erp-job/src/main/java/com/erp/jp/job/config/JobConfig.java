package com.erp.jp.job.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

/**
 * 定时任务配置
 * [by Agent]
 */
@Configuration
@EnableScheduling
public class JobConfig implements SchedulingConfigurer {

    /**
     * 配置任务调度线程池
     * 使用 10 个线程的线程池执行定时任务
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(
            Executors.newScheduledThreadPool(10, r -> {
                Thread thread = new Thread(r);
                thread.setName("erp-job-scheduler-" + thread.getId());
                thread.setDaemon(true);
                return thread;
            })
        );
    }
}
