package com.erp.jp.store.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 供应商主表
 * [by Agent]
 */
@Data
@TableName("t_supplier")
public class Supplier {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 租户 ID */
    private Long tenantId;

    /** 供应商编码 */
    private String supplierCode;

    /** 供应商名称 */
    private String supplierName;

    /** 供应商类型：1-工厂 2-贸易商 3-个人 */
    private Integer supplierType;

    /** 国家代码 */
    private String countryCode;

    /** 结算币种 */
    private String settlementCurrency;

    /** 日本发票注册号 */
    private String invoiceRegistrationNumber;

    /** 默认采购提前期（天） */
    private Integer leadTimeDays;

    /** 综合评分 */
    private BigDecimal ratingScore;

    /** 评级等级：S/A/B/C/D */
    private String ratingLevel;

    /** 状态：0-禁用 1-启用 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
