package com.erp.jp.inventory.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.erp.jp.common.result.R;
import com.erp.jp.inventory.dto.InventoryDTO;
import com.erp.jp.inventory.entity.Inventory;
import com.erp.jp.inventory.entity.InventoryTransaction;
import com.erp.jp.inventory.entity.InventoryWarning;
import com.erp.jp.inventory.mapper.InventoryMapper;
import com.erp.jp.inventory.mapper.InventoryTransactionMapper;
import com.erp.jp.inventory.mapper.InventoryWarningMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 库存服务 - 四态模型
 * [by Agent]
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryMapper inventoryMapper;
    private final InventoryTransactionMapper transactionMapper;
    private final InventoryWarningMapper warningMapper;

    /**
     * 查询库存（四态）
     */
    public R<List<InventoryDTO>> listInventory(Long tenantId, Long warehouseId, Long skuId) {
        if (tenantId == null) tenantId = 1L;

        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<Inventory>()
                .eq(Inventory::getTenantId, tenantId)
                .eq(warehouseId != null, Inventory::getWarehouseId, warehouseId)
                .eq(skuId != null, Inventory::getSkuId, skuId);

        List<Inventory> list = inventoryMapper.selectList(wrapper);

        List<InventoryDTO> dtoList = list.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return R.ok(dtoList);
    }

    /**
     * 库存入库
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> stockIn(Long tenantId, Long warehouseId, Long skuId, Integer qty,
                           String batchNo, String locationCode, String refNo, String operator) {
        if (qty <= 0) {
            return R.fail("入库数量必须大于 0");
        }

        // 查询现有库存
        Inventory inventory = getOrCreateInventory(tenantId, warehouseId, skuId, batchNo, locationCode);

        // 记录变动前库存
        int physicalBefore = inventory.getQtyPhysical() != null ? inventory.getQtyPhysical() : 0;
        int availableBefore = inventory.getQtyAvailable() != null ? inventory.getQtyAvailable() : 0;

        // 更新库存
        inventory.setQtyPhysical(physicalBefore + qty);
        inventory.setQtyAvailable(availableBefore + qty);
        inventory.setUpdateTime(LocalDateTime.now());
        inventoryMapper.updateById(inventory);

        // 记录流水
        recordTransaction(tenantId, warehouseId, skuId, batchNo, "IN", qty,
                physicalBefore, physicalBefore + qty,
                availableBefore, availableBefore + qty,
                refNo, operator, "采购入库");

        log.info("库存入库：warehouseId={}, skuId={}, qty={}, batchNo={}", warehouseId, skuId, qty, batchNo);
        return R.ok();
    }

    /**
     * 库存出库
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> stockOut(Long tenantId, Long warehouseId, Long skuId, Integer qty,
                            String batchNo, String refNo, String operator) {
        if (qty <= 0) {
            return R.fail("出库数量必须大于 0");
        }

        Inventory inventory = getInventory(tenantId, warehouseId, skuId, batchNo);
        if (inventory == null) {
            return R.fail("库存不存在");
        }

        int available = inventory.getQtyAvailable() != null ? inventory.getQtyAvailable() : 0;
        if (available < qty) {
            return R.fail("可用库存不足");
        }

        int physicalBefore = inventory.getQtyPhysical() != null ? inventory.getQtyPhysical() : 0;
        int availableBefore = available;

        // 更新库存
        inventory.setQtyPhysical(physicalBefore - qty);
        inventory.setQtyAvailable(available - qty);
        inventory.setUpdateTime(LocalDateTime.now());
        inventoryMapper.updateById(inventory);

        // 记录流水
        recordTransaction(tenantId, warehouseId, skuId, batchNo, "OUT", -qty,
                physicalBefore, physicalBefore - qty,
                availableBefore, available - qty,
                refNo, operator, "销售出库");

        log.info("库存出库：warehouseId={}, skuId={}, qty={}, batchNo={}", warehouseId, skuId, qty, batchNo);
        return R.ok();
    }

    /**
     * 库存锁定（下单时）
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> lockInventory(Long tenantId, Long warehouseId, Long skuId, Integer qty,
                                 String batchNo, String refNo, String operator) {
        if (qty <= 0) {
            return R.fail("锁定数量必须大于 0");
        }

        Inventory inventory = getInventory(tenantId, warehouseId, skuId, batchNo);
        if (inventory == null) {
            return R.fail("库存不存在");
        }

        int available = inventory.getQtyAvailable() != null ? inventory.getQtyAvailable() : 0;
        int locked = inventory.getQtyLocked() != null ? inventory.getQtyLocked() : 0;

        if (available < qty) {
            return R.fail("可用库存不足");
        }

        // 更新库存：可用减少，锁定增加
        inventory.setQtyAvailable(available - qty);
        inventory.setQtyLocked(locked + qty);
        inventory.setUpdateTime(LocalDateTime.now());
        inventoryMapper.updateById(inventory);

        log.info("库存锁定：warehouseId={}, skuId={}, qty={}, refNo={}", warehouseId, skuId, qty, refNo);
        return R.ok();
    }

    /**
     * 库存解锁（取消订单）
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> unlockInventory(Long tenantId, Long warehouseId, Long skuId, Integer qty,
                                   String batchNo, String refNo, String operator) {
        if (qty <= 0) {
            return R.fail("解锁数量必须大于 0");
        }

        Inventory inventory = getInventory(tenantId, warehouseId, skuId, batchNo);
        if (inventory == null) {
            return R.fail("库存不存在");
        }

        int locked = inventory.getQtyLocked() != null ? inventory.getQtyLocked() : 0;
        if (locked < qty) {
            return R.fail("锁定库存不足");
        }

        // 更新库存：锁定减少，可用增加
        inventory.setQtyLocked(locked - qty);
        inventory.setQtyAvailable((inventory.getQtyAvailable() != null ? inventory.getQtyAvailable() : 0) + qty);
        inventory.setUpdateTime(LocalDateTime.now());
        inventoryMapper.updateById(inventory);

        log.info("库存解锁：warehouseId={}, skuId={}, qty={}, refNo={}", warehouseId, skuId, qty, refNo);
        return R.ok();
    }

    /**
     * 查询在途库存
     */
    public R<Integer> getIntransitQty(Long tenantId, Long warehouseId, Long skuId) {
        Inventory inventory = getInventory(tenantId, warehouseId, skuId, null);
        int intransit = inventory != null && inventory.getQtyIntransit() != null ? inventory.getQtyIntransit() : 0;
        return R.ok(intransit);
    }

    /**
     * 更新在途库存（采购发货时）
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> updateIntransitQty(Long tenantId, Long warehouseId, Long skuId, Integer qty,
                                      String refNo, String operator) {
        Inventory inventory = getOrCreateInventory(tenantId, warehouseId, skuId, null, null);
        int intransitBefore = inventory.getQtyIntransit() != null ? inventory.getQtyIntransit() : 0;
        inventory.setQtyIntransit(intransitBefore + qty);
        inventory.setUpdateTime(LocalDateTime.now());
        inventoryMapper.updateById(inventory);

        log.info("在途库存更新：warehouseId={}, skuId={}, qty={}, refNo={}", warehouseId, skuId, qty, refNo);
        return R.ok();
    }

    // 辅助方法
    private Inventory getInventory(Long tenantId, Long warehouseId, Long skuId, String batchNo) {
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<Inventory>()
                .eq(Inventory::getTenantId, tenantId)
                .eq(Inventory::getWarehouseId, warehouseId)
                .eq(Inventory::getSkuId, skuId);

        if (batchNo != null) {
            wrapper.eq(Inventory::getBatchNo, batchNo);
        }

        return inventoryMapper.selectOne(wrapper);
    }

    private Inventory getOrCreateInventory(Long tenantId, Long warehouseId, Long skuId,
                                           String batchNo, String locationCode) {
        Inventory inventory = getInventory(tenantId, warehouseId, skuId, batchNo);
        if (inventory == null) {
            inventory = new Inventory();
            inventory.setTenantId(tenantId);
            inventory.setWarehouseId(warehouseId);
            inventory.setSkuId(skuId);
            inventory.setBatchNo(batchNo);
            inventory.setLocationCode(locationCode);
            inventory.setQtyPhysical(0);
            inventory.setQtyAvailable(0);
            inventory.setQtyLocked(0);
            inventory.setQtyIntransit(0);
            inventory.setCreateTime(LocalDateTime.now());
            inventory.setUpdateTime(LocalDateTime.now());
            inventoryMapper.insert(inventory);
        }
        return inventory;
    }

    private void recordTransaction(Long tenantId, Long warehouseId, Long skuId, String batchNo,
                                   String transType, Integer qtyChange,
                                   int physicalBefore, int physicalAfter,
                                   int availableBefore, int availableAfter,
                                   String refNo, String operator, String remark) {
        InventoryTransaction trans = new InventoryTransaction();
        trans.setTenantId(tenantId);
        trans.setWarehouseId(warehouseId);
        trans.setSkuId(skuId);
        trans.setBatchNo(batchNo);
        trans.setTransType(transType);
        trans.setQtyChange(qtyChange);
        trans.setQtyPhysicalBefore(physicalBefore);
        trans.setQtyPhysicalAfter(physicalAfter);
        trans.setQtyAvailableBefore(availableBefore);
        trans.setQtyAvailableAfter(availableAfter);
        trans.setRefNo(refNo);
        trans.setOperator(operator);
        trans.setRemark(remark);
        trans.setCreateTime(LocalDateTime.now());
        transactionMapper.insert(trans);
    }

    private InventoryDTO toDTO(Inventory inventory) {
        InventoryDTO dto = new InventoryDTO();
        dto.setId(inventory.getId());
        dto.setWarehouseId(inventory.getWarehouseId());
        dto.setSkuId(inventory.getSkuId());
        dto.setBatchNo(inventory.getBatchNo());
        dto.setLocationCode(inventory.getLocationCode());
        dto.setQtyPhysical(inventory.getQtyPhysical());
        dto.setQtyAvailable(inventory.getQtyAvailable());
        dto.setQtyLocked(inventory.getQtyLocked());
        dto.setQtyIntransit(inventory.getQtyIntransit());
        dto.setExpiryDate(inventory.getExpiryDate());
        return dto;
    }

    /**
     * 检查库存预警
     * [by Agent]
     */
    public void checkInventoryWarning(Long tenantId, Long warehouseId, Long skuId, Integer availableQty) {
        // 库存不足预警（阈值：10）
        if (availableQty != null && availableQty < 10) {
            createWarning(tenantId, warehouseId, skuId, 1, availableQty, 10, 3);
        }
        // 库存积压预警（阈值：1000）
        if (availableQty != null && availableQty > 1000) {
            createWarning(tenantId, warehouseId, skuId, 2, availableQty, 1000, 2);
        }
    }

    private void createWarning(Long tenantId, Long warehouseId, Long skuId,
                               Integer warningType, Integer currentQty, Integer thresholdQty, Integer warningLevel) {
        InventoryWarning warning = new InventoryWarning();
        warning.setTenantId(tenantId);
        warning.setWarehouseId(warehouseId);
        warning.setSkuId(skuId);
        warning.setWarningType(warningType);
        warning.setCurrentQty(currentQty);
        warning.setThresholdQty(thresholdQty);
        warning.setWarningLevel(warningLevel);
        warning.setIsHandled(0);
        warning.setCreateTime(LocalDateTime.now());
        warningMapper.insert(warning);
        log.info("库存预警创建：warehouseId={}, skuId={}, type={}, qty={}", warehouseId, skuId, warningType, currentQty);
    }
}
