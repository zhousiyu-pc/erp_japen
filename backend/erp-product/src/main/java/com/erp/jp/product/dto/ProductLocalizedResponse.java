package com.erp.jp.product.dto;

import lombok.Data;

/**
 * 多语言文案响应 DTO
 */
@Data
public class ProductLocalizedResponse {
    
    private Long id;
    
    private Long spuId;
    
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
