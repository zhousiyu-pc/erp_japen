package com.erp.jp.inventory.controller;

import com.erp.jp.common.result.R;
import com.erp.jp.inventory.dto.InventoryDTO;
import com.erp.jp.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存管理 API
 * [by Agent]
 */
@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * 查询库存列表
     */
    @GetMapping("/inventory")
    public R<List<InventoryDTO>> listInventory(
            @RequestParam(required = false) Long tenantId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long skuId) {
        return inventoryService.listInventory(tenantId, warehouseId, skuId);
    }

    /**
     * 库存入库
     */
    @PostMapping("/inventory/in")
    public R<Void> stockIn(
            @RequestParam Long warehouseId,
            @RequestParam Long skuId,
            @RequestParam Integer qty,
            @RequestParam(required = false) String batchNo,
            @RequestParam(required = false) String locationCode,
            @RequestParam(required = false) String refNo,
            @RequestParam(required = false) String operator) {
        return inventoryService.stockIn(1L, warehouseId, skuId, qty, batchNo, locationCode, refNo, operator);
    }

    /**
     * 库存出库
     */
    @PostMapping("/inventory/out")
    public R<Void> stockOut(
            @RequestParam Long warehouseId,
            @RequestParam Long skuId,
            @RequestParam Integer qty,
            @RequestParam(required = false) String batchNo,
            @RequestParam(required = false) String refNo,
            @RequestParam(required = false) String operator) {
        return inventoryService.stockOut(1L, warehouseId, skuId, qty, batchNo, refNo, operator);
    }

    /**
     * 库存锁定
     */
    @PostMapping("/inventory/lock")
    public R<Void> lockInventory(
            @RequestParam Long warehouseId,
            @RequestParam Long skuId,
            @RequestParam Integer qty,
            @RequestParam(required = false) String batchNo,
            @RequestParam String refNo,
            @RequestParam(required = false) String operator) {
        return inventoryService.lockInventory(1L, warehouseId, skuId, qty, batchNo, refNo, operator);
    }

    /**
     * 库存解锁
     */
    @PostMapping("/inventory/unlock")
    public R<Void> unlockInventory(
            @RequestParam Long warehouseId,
            @RequestParam Long skuId,
            @RequestParam Integer qty,
            @RequestParam(required = false) String batchNo,
            @RequestParam String refNo,
            @RequestParam(required = false) String operator) {
        return inventoryService.unlockInventory(1L, warehouseId, skuId, qty, batchNo, refNo, operator);
    }

    /**
     * 查询在途库存
     */
    @GetMapping("/inventory/intransit")
    public R<Integer> getIntransitQty(
            @RequestParam Long warehouseId,
            @RequestParam Long skuId) {
        return inventoryService.getIntransitQty(1L, warehouseId, skuId);
    }
}
