package com.erp.jp.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 店铺商品映射表 (Listing)
 */
@Data
@TableName("pim_product_listing")
public class ProductListing {
    
    @TableId(type = IdType.AUTO)
    private Long listingId;
    
    private Long tenantId;
    
    private Long spuId;
    
    private Long skuId;
    
    private Long shopId;
    
    /**
     * 平台 SKU ID
     */
    private String platformSkuId;
    
    /**
     * 平台商品 ID（如 ASIN）
     */
    private String platformProductId;
    
    /**
     * 同步状态：0-待同步 1-同步中 2-成功 3-失败
     */
    private Integer syncStatus;
    
    /**
     * 最后同步时间
     */
    private LocalDateTime lastSyncTime;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer isDeleted;
}
