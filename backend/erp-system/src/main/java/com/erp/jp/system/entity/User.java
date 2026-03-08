package com.erp.jp.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户表
 */
@Data
@TableName("sys_user")
public class User {
    
    @TableId(type = IdType.AUTO)
    private Long userId;
    
    private Long tenantId;
    
    private Long deptId;
    
    /**
     * 登录账号
     */
    private String userName;
    
    /**
     * 真实姓名（支持日文）
     */
    private String realName;
    
    /**
     * 密码（BCrypt 加密）
     */
    private String password;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 状态：0-正常 1-停用
     */
    private Integer status;
    
    /**
     * 最后登录 IP
     */
    private String loginIp;
    
    /**
     * 最后登录时间
     */
    private LocalDateTime loginDate;
    
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer isDeleted;
}
