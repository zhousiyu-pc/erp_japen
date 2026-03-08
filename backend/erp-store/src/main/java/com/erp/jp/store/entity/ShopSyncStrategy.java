package com.erp.jp.store.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 店铺同步策略
 */
@Data
@TableName("t_shop_sync_strategy")
public class ShopSyncStrategy {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long shopId;
    private Long tenantId;
    private String syncModule;  // ORDER/INVENTORY/PRODUCT
    private Integer syncFrequency;
    private String cronExpression;
    private String configJson;
    private Integer status;
    private LocalDateTime lastSyncTime;
    private LocalDateTime nextSyncTime;
    private String createBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
