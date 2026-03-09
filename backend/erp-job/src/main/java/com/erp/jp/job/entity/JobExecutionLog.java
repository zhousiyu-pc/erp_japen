package com.erp.jp.job.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务执行日志
 * [by Agent]
 */
@Data
@TableName("t_job_execution_log")
public class JobExecutionLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 任务名称 */
    private String jobName;

    /** 任务类型：ORDER_SYNC/INVENTORY_SYNC/PRODUCT_SYNC */
    private String jobType;

    /** 执行状态：0-执行中 1-成功 2-失败 */
    private Integer status;

    /** 开始时间 */
    private LocalDateTime startTime;

    /** 结束时间 */
    private LocalDateTime endTime;

    /** 拉取数量 */
    private Integer fetchCount;

    /** 成功数量 */
    private Integer successCount;

    /** 失败数量 */
    private Integer failCount;

    /** 错误信息 */
    private String errorMessage;

    /** 原始响应 JSON */
    private String rawResponse;

    /** 创建时间 */
    private LocalDateTime createTime;
}
