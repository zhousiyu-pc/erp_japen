package com.erp.jp.store.controller;

import com.erp.jp.common.result.R;
import com.erp.jp.store.dto.SyncConfigDTO;
import com.erp.jp.store.service.SyncConfigService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 同步策略配置 API
 * [by Agent]
 */
@RestController
@RequestMapping("/api/v1/store")
public class SyncConfigController {

    private final SyncConfigService syncConfigService;

    public SyncConfigController(SyncConfigService syncConfigService) {
        this.syncConfigService = syncConfigService;
    }

    /**
     * 获取同步策略配置
     */
    @GetMapping("/sync-config")
    public R<SyncConfigDTO> getSyncConfig(@RequestParam(required = false) Long tenantId) {
        return syncConfigService.getConfig(tenantId);
    }

    /**
     * 保存同步策略配置
     */
    @PostMapping("/sync-config")
    public R<Void> saveSyncConfig(@RequestParam(required = false) Long tenantId, @RequestBody SyncConfigDTO dto) {
        return syncConfigService.saveConfig(tenantId, dto);
    }

    /**
     * 获取店铺同步策略列表
     */
    @GetMapping("/shops/{shopId}/sync-strategies")
    public R<List<SyncConfigDTO>> listShopStrategies(@PathVariable Long shopId) {
        return syncConfigService.listShopStrategies(shopId);
    }

    /**
     * 创建店铺同步策略
     */
    @PostMapping("/shops/{shopId}/sync-strategies")
    public R<Long> createShopStrategy(@PathVariable Long shopId, @RequestBody SyncConfigDTO dto) {
        dto.setShopId(shopId);
        return syncConfigService.createStrategy(dto);
    }

    /**
     * 更新同步策略
     */
    @PutMapping("/sync-strategies/{id}")
    public R<Void> updateStrategy(@PathVariable Long id, @RequestBody SyncConfigDTO dto) {
        dto.setId(id);
        return syncConfigService.updateStrategy(dto);
    }

    /**
     * 删除同步策略
     */
    @DeleteMapping("/sync-strategies/{id}")
    public R<Void> deleteStrategy(@PathVariable Long id) {
        return syncConfigService.deleteStrategy(id);
    }

    /**
     * 启用/禁用同步策略
     */
    @PutMapping("/sync-strategies/{id}/status")
    public R<Void> updateStrategyStatus(@PathVariable Long id, @RequestParam Integer status) {
        return syncConfigService.updateStrategyStatus(id, status);
    }

    /**
     * 手动触发同步
     */
    @PostMapping("/shops/{shopId}/sync")
    public R<Void> manualSync(@PathVariable Long shopId, @RequestParam String module) {
        return syncConfigService.manualSync(shopId, module);
    }
}
