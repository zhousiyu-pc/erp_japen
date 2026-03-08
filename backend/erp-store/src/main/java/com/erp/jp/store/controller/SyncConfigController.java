package com.erp.jp.store.controller;

import com.erp.jp.common.result.R;
import com.erp.jp.store.dto.SyncConfigDTO;
import com.erp.jp.store.service.SyncConfigService;
import org.springframework.web.bind.annotation.*;

/**
 * 同步策略配置 API
 */
@RestController
@RequestMapping("/api/v1/store")
public class SyncConfigController {

    private final SyncConfigService syncConfigService;

    public SyncConfigController(SyncConfigService syncConfigService) {
        this.syncConfigService = syncConfigService;
    }

    @GetMapping("/sync-config")
    public R<SyncConfigDTO> getConfig(@RequestParam(required = false) Long tenantId) {
        return syncConfigService.getConfig(tenantId);
    }

    @PostMapping("/sync-config")
    public R<Void> saveConfig(@RequestParam(required = false) Long tenantId, @RequestBody SyncConfigDTO dto) {
        return syncConfigService.saveConfig(tenantId, dto);
    }
}
