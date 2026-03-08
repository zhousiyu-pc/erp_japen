package com.erp.jp.system.dto;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 租户注册请求 DTO
 */
@Data
public class TenantRegisterRequest {
    
    @NotBlank(message = "企业名称不能为空")
    private String tenantName;
    
    @NotBlank(message = "管理员邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String adminEmail;
    
    @NotBlank(message = "管理员手机号不能为空")
    private String adminPhone;
    
    private String industryType = "B2C_B2B";
    
    @NotBlank(message = "套餐代码不能为空")
    private String planCode;
    
    private String regionCode = "JP";
    
    private String taxId;
}
