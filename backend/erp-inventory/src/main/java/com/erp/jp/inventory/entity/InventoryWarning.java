package com.erp.jp.inventory.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 库存预警表
 * [by Agent]
 */
@Data
@TableName("t_inventory_warning")
public class InventoryWarning {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 租户 ID */
    private Long tenantId;

    /** 仓库 ID */
    private Long warehouseId;

    /** SKU ID */
    private Long skuId;

    /** 预警类型：1-库存不足 2-库存积压 3-效期预警 */
    private Integer warningType;

    /** 当前库存 */
    private Integer currentQty;

    /** 阈值 */
    private Integer thresholdQty;

    /** 预警级别：1-低 2-中 3-高 */
    private Integer warningLevel;

    /** 是否已处理 */
    private Integer isHandled;

    /** 处理人 */
    private String handler;

    /** 处理时间 */
    private LocalDateTime handleTime;

    /** 处理备注 */
    private String handleRemark;

    /** 创建时间 */
    private LocalDateTime createTime;
}
