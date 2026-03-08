package com.erp.jp.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 租户主表
 */
@Data
@TableName("sys_tenant")
public class Tenant {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 租户编码
     */
    private String tenantCode;
    
    /**
     * 企业名称
     */
    private String tenantName;
    
    /**
     * 套餐 ID
     */
    private Long planId;
    
    /**
     * 状态：0-禁用 1-正常 2-欠费锁定
     */
    private Integer status;
    
    /**
     * 服务到期时间
     */
    private LocalDateTime expireTime;
    
    /**
     * 纳税识别号（日本法人番号）
     */
    private String taxId;
    
    /**
     * 管理员邮箱
     */
    private String adminEmail;
    
    /**
     * 管理员手机号
     */
    private String adminPhone;
    
    /**
     * 业务模式：B2C, B2B, B2C_B2B
     */
    private String industryType;
    
    /**
     * 目标市场区域
     */
    private String regionCode;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer isDeleted;
}
