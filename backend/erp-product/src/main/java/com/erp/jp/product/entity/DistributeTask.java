package com.erp.jp.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 分发任务表
 */
@Data
@TableName("sys_distribute_task")
public class DistributeTask {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 任务唯一 ID
     */
    private String taskId;
    
    private Long tenantId;
    
    /**
     * 任务类型：BATCH_UPDATE/DISTRIBUTE
     */
    private String taskType;
    
    /**
     * 总数量
     */
    private Integer totalCount;
    
    /**
     * 成功数量
     */
    private Integer successCount;
    
    /**
     * 失败数量
     */
    private Integer failCount;
    
    /**
     * 状态：0-待执行 1-执行中 2-完成 3-失败
     */
    private Integer status;
    
    /**
     * 错误日志 URL
     */
    private String errorLogUrl;
    
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
