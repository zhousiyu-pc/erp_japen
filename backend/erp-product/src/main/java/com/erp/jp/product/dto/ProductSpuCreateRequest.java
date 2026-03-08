package com.erp.jp.product.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 商品 SPU 创建请求 DTO
 */
@Data
public class ProductSpuCreateRequest {
    
    @NotBlank(message = "SPU 编码不能为空")
    private String spuCode;
    
    @NotBlank(message = "商品名称不能为空")
    private String productName;
    
    private Long categoryId;
    
    private Long brandId;
    
    @NotNull(message = "价格不能为空")
    private BigDecimal basePrice;
    
    private String currency = "JPY";
    
    private Integer status = 0; // 默认草稿
}
