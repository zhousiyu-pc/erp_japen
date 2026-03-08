package com.erp.jp.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 用户角色关联表
 */
@Data
@TableName("sys_user_role")
public class UserRole {
    
    /**
     * 用户 ID
     */
    private Long userId;
    
    /**
     * 角色 ID
     */
    private Long roleId;
}
