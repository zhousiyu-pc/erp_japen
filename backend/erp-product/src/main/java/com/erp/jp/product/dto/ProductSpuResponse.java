package com.erp.jp.product.dto;

import lombok.Data;

/**
 * 商品 SPU 响应 DTO
 */
@Data
public class ProductSpuResponse {
    
    private Long spuId;
    
    private String spuCode;
    
    private String productName;
    
    private Long categoryId;
    
    private Long brandId;
    
    private String basePrice;
    
    private String currency;
    
    private Integer status;
    
    private Integer skuCount; // SKU 数量
}
