package com.erp.jp.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品 SPU 主表
 */
@Data
@TableName("pim_product_spu")
public class ProductSpu {
    
    @TableId(type = IdType.AUTO)
    private Long spuId;
    
    private Long tenantId;
    
    /**
     * SPU 编码
     */
    private String spuCode;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 类目 ID
     */
    private Long categoryId;
    
    /**
     * 品牌 ID
     */
    private Long brandId;
    
    /**
     * 基础售价
     */
    private BigDecimal basePrice;
    
    /**
     * 币种 (默认 JPY)
     */
    private String currency;
    
    /**
     * 状态：0-草稿 1-在售 2-停售
     */
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer isDeleted;
}
