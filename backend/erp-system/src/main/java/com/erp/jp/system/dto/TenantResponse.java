package com.erp.jp.system.dto;

import lombok.Data;

/**
 * 租户响应 DTO
 */
@Data
public class TenantResponse {
    
    private Long tenantId;
    
    private String tenantCode;
    
    private String tenantName;
    
    private String adminEmail;
    
    private String adminPhone;
    
    private String industryType;
    
    private String regionCode;
    
    private String taxId;
    
    private Integer status;
    
    private String expireTime;
}
