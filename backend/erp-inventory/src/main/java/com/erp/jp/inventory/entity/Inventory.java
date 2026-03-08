package com.erp.jp.inventory.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 库存主表 - 四态模型
 * [by Agent]
 */
@Data
@TableName("t_inventory")
public class Inventory {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 租户 ID */
    private Long tenantId;

    /** 仓库 ID */
    private Long warehouseId;

    /** 商品 ID */
    private Long productId;

    /** SKU ID */
    private Long skuId;

    /** 实物库存 */
    private Integer qtyPhysical;

    /** 可用库存 */
    private Integer qtyAvailable;

    /** 锁定库存（已下单未发货） */
    private Integer qtyLocked;

    /** 在途库存（采购中） */
    private Integer qtyIntransit;

    /** 批次号 */
    private String batchNo;

    /** 生产日期 */
    private LocalDateTime productionDate;

    /** 有效期至 */
    private LocalDateTime expiryDate;

    /** 库位编码 */
    private String locationCode;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
