package com.erp.jp.job.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * 重试配置
 * [by Agent]
 */
@Configuration
@EnableRetry
public class RetryConfig {

    /**
     * 创建重试模板
     * 默认配置：重试 3 次，每次间隔 5 秒
     */
    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        // 重试策略：最多重试 3 次
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(4); // 1 次初始 + 3 次重试
        retryTemplate.setRetryPolicy(retryPolicy);

        // 回退策略：固定间隔 5 秒
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(5000L); // 5 秒
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }
}
