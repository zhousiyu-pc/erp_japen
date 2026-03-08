package com.erp.jp.inventory.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 库存 DTO
 * [by Agent]
 */
@Data
public class InventoryDTO {

    private Long id;
    private Long warehouseId;
    private Long skuId;
    private String batchNo;
    private String locationCode;
    private Integer qtyPhysical;
    private Integer qtyAvailable;
    private Integer qtyLocked;
    private Integer qtyIntransit;
    private LocalDateTime expiryDate;
}
