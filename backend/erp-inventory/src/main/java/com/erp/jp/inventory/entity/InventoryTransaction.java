package com.erp.jp.inventory.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存流水表
 * [by Agent]
 */
@Data
@TableName("t_inventory_transaction")
public class InventoryTransaction {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 租户 ID */
    private Long tenantId;

    /** 仓库 ID */
    private Long warehouseId;

    /** SKU ID */
    private Long skuId;

    /** 批次号 */
    private String batchNo;

    /** 变动类型：IN-入库 OUT-出库 LOCK-锁定 UNLOCK-解锁 TRANSFER-调拨 */
    private String transType;

    /** 变动数量（正数增加，负数减少） */
    private Integer qtyChange;

    /** 变动前实物库存 */
    private Integer qtyPhysicalBefore;

    /** 变动后实物库存 */
    private Integer qtyPhysicalAfter;

    /** 变动前可用库存 */
    private Integer qtyAvailableBefore;

    /** 变动后可用库存 */
    private Integer qtyAvailableAfter;

    /** 关联单据号（采购单/订单/调拨单） */
    private String refNo;

    /** 操作人 */
    private String operator;

    /** 备注 */
    private String remark;

    /** 创建时间 */
    private LocalDateTime createTime;
}
