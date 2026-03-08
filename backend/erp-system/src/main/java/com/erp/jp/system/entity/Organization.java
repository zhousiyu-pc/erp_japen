package com.erp.jp.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 组织架构表
 */
@Data
@TableName("sys_organization")
public class Organization {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long tenantId;
    
    /**
     * 组织日文名称
     */
    private String orgNameJa;
    
    /**
     * 组织英文名称
     */
    private String orgNameEn;
    
    /**
     * 组织编码
     */
    private String orgCode;
    
    /**
     * 父级组织 ID
     */
    private Long parentId;
    
    /**
     * 祖级列表 (逗号分隔)
     */
    private String ancestors;
    
    /**
     * 负责人 ID
     */
    private Long leaderId;
    
    /**
     * 排序
     */
    private Integer sort;
    
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
