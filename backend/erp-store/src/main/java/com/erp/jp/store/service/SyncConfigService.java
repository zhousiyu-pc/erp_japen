package com.erp.jp.store.service;

import com.erp.jp.common.result.R;
import com.erp.jp.store.dto.SyncConfigDTO;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 同步策略配置服务 - 暂用内存存储，后续可落库
 */
@Service
public class SyncConfigService {

    private final ConcurrentHashMap<Long, SyncConfigDTO> configMap = new ConcurrentHashMap<>();

    public R<SyncConfigDTO> getConfig(Long tenantId) {
        if (tenantId == null) tenantId = 1L;
        SyncConfigDTO dto = configMap.get(tenantId);
        if (dto == null) {
            dto = new SyncConfigDTO();
            dto.setOrderSyncRate("15");
            dto.setStockSyncStrategy("realtime");
            dto.setStockReserve(10);
            dto.setFilterTestOrders(true);
            dto.setGlobalSyncEnabled(true);
        }
        return R.ok(dto);
    }

    public R<Void> saveConfig(Long tenantId, SyncConfigDTO dto) {
        if (tenantId == null) tenantId = 1L;
        configMap.put(tenantId, dto);
        return R.ok();
    }
}
