package com.erp.jp.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 角色表
 */
@Data
@TableName("sys_role")
public class Role {
    
    @TableId(type = IdType.AUTO)
    private Long roleId;
    
    private Long tenantId;
    
    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 角色编码
     */
    private String roleCode;
    
    /**
     * 数据范围：1-全部 2-自定义 3-本部门 4-本部门及下级 5-仅本人
     */
    private Integer dataScope;
    
    /**
     * 状态：0-正常 1-停用
     */
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer isDeleted;
}
