package com.erp.jp.product.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 商品 SKU 创建请求 DTO
 */
@Data
public class ProductSkuCreateRequest {
    
    @NotNull(message = "SPU ID 不能为空")
    private Long spuId;
    
    @NotBlank(message = "SKU 编码不能为空")
    private String skuCode;
    
    private String janCode;
    
    @NotNull(message = "成本价不能为空")
    private BigDecimal costPrice;
    
    @NotNull(message = "售价不能为空")
    private BigDecimal salePrice;
    
    private BigDecimal weight;
    
    private String variantAttrs;
    
    private Integer status = 1;
}
