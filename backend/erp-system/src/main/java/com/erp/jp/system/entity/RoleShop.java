package com.erp.jp.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 角色店铺权限关联表
 */
@Data
@TableName("sys_role_shop")
public class RoleShop {
    
    /**
     * 角色 ID
     */
    private Long roleId;
    
    /**
     * 店铺 ID
     */
    private Long shopId;
}
