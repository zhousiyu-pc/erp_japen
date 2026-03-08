package com.erp.jp.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 商品多语言文案表
 */
@Data
@TableName("pim_product_localized")
public class ProductLocalized {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long tenantId;
    
    private Long spuId;
    
    /**
     * 语言：ja_JP/zh_CN/en_US
     */
    private String locale;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 副标题
     */
    private String subtitle;
    
    /**
     * 卖点 1-5
     */
    private String bullet1;
    private String bullet2;
    private String bullet3;
    private String bullet4;
    private String bullet5;
    
    /**
     * 商品描述
     */
    private String description;
    
    /**
     * 搜索关键词
     */
    private String searchTerms;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
