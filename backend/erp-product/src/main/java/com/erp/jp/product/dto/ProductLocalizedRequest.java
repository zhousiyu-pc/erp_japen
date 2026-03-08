package com.erp.jp.product.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 多语言文案请求 DTO
 */
@Data
public class ProductLocalizedRequest {
    
    @NotNull(message = "SPU ID 不能为空")
    private Long spuId;
    
    @NotBlank(message = "语言不能为空")
    private String locale;
    
    private String title;
    
    private String subtitle;
    
    private String bullet1;
    private String bullet2;
    private String bullet3;
    private String bullet4;
    private String bullet5;
    
    private String description;
    
    private String searchTerms;
}
