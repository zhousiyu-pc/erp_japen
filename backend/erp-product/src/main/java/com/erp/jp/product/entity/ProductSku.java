package com.erp.jp.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品 SKU 表
 */
@Data
@TableName("pim_product_sku")
public class ProductSku {
    
    @TableId(type = IdType.AUTO)
    private Long skuId;
    
    private Long tenantId;
    
    private Long spuId;
    
    /**
     * SKU 编码
     */
    private String skuCode;
    
    /**
     * JAN 码 (日本商品编码)
     */
    private String janCode;
    
    /**
     * 成本价
     */
    private BigDecimal costPrice;
    
    /**
     * 售价
     */
    private BigDecimal salePrice;
    
    /**
     * 重量 (g)
     */
    private BigDecimal weight;
    
    /**
     * 变体属性 JSON 如{"color":"黑","size":"M"}
     */
    private String variantAttrs;
    
    /**
     * 状态：0-草稿 1-在售 2-停售
     */
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer isDeleted;
}
