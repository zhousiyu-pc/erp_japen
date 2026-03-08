package com.erp.jp.product.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 商品 SKU 响应 DTO
 */
@Data
public class ProductSkuResponse {
    
    private Long skuId;
    
    private Long spuId;
    
    private String skuCode;
    
    private String janCode;
    
    private String costPrice;
    
    private String salePrice;
    
    private BigDecimal weight;
    
    private String variantAttrs;
    
    private Integer status;
}
