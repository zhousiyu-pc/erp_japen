package com.erp.jp.supplier.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购单主表
 * [by Agent]
 */
@Data
@TableName("t_purchase_order")
public class PurchaseOrder {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 采购单号 */
    private String orderNo;

    /** 租户 ID */
    private Long tenantId;

    /** 供应商 ID */
    private Long supplierId;

    /** 仓库 ID */
    private Long warehouseId;

    /** 采购币种 */
    private String currency;

    /** 汇率（原币->JPY） */
    private BigDecimal exchangeRate;

    /** 原币总金额 */
    private BigDecimal totalAmountOrigin;

    /** JPY 总金额 */
    private BigDecimal totalAmountJpy;

    /** 订单状态：10-待审核 20-已审核 30-部分到货 40-已完成 50-已取消 */
    private Integer orderStatus;

    /** 预计到货日期 */
    private LocalDateTime estimatedArrivalDate;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
