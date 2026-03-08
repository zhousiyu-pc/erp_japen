package com.erp.jp.store.service;

import com.erp.jp.common.result.R;
import com.erp.jp.store.dto.SyncConfigDTO;
import com.erp.jp.store.entity.ShopSyncStrategy;
import com.erp.jp.store.mapper.ShopSyncStrategyMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 同步策略配置服务
 * [by Agent]
 */
@Service
public class SyncConfigService {

    private final ShopSyncStrategyMapper shopSyncStrategyMapper;
    private final ConcurrentHashMap<Long, SyncConfigDTO> configMap = new ConcurrentHashMap<>();

    public SyncConfigService(ShopSyncStrategyMapper shopSyncStrategyMapper) {
        this.shopSyncStrategyMapper = shopSyncStrategyMapper;
    }

    /**
     * 获取同步策略配置
     */
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

    /**
     * 保存同步策略配置
     */
    public R<Void> saveConfig(Long tenantId, SyncConfigDTO dto) {
        if (tenantId == null) tenantId = 1L;
        configMap.put(tenantId, dto);
        return R.ok();
    }

    /**
     * 获取店铺同步策略列表
     */
    public R<List<SyncConfigDTO>> listShopStrategies(Long shopId) {
        if (shopId == null) {
            return R.fail("店铺 ID 不能为空");
        }
        
        List<ShopSyncStrategy> strategies = shopSyncStrategyMapper.selectList(
                new LambdaQueryWrapper<ShopSyncStrategy>()
                        .eq(ShopSyncStrategy::getShopId, shopId)
                        .orderByAsc(ShopSyncStrategy::getSyncModule));
        
        List<SyncConfigDTO> result = new ArrayList<>();
        for (ShopSyncStrategy s : strategies) {
            SyncConfigDTO dto = toDTO(s);
            result.add(dto);
        }
        return R.ok(result);
    }

    /**
     * 创建店铺同步策略
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Long> createStrategy(SyncConfigDTO dto) {
        if (dto.getShopId() == null) {
            return R.fail("店铺 ID 不能为空");
        }
        if (dto.getSyncModule() == null) {
            return R.fail("同步模块不能为空");
        }
        
        // 检查是否已存在该模块的策略
        Long count = shopSyncStrategyMapper.selectCount(
                new LambdaQueryWrapper<ShopSyncStrategy>()
                        .eq(ShopSyncStrategy::getShopId, dto.getShopId())
                        .eq(ShopSyncStrategy::getSyncModule, dto.getSyncModule()));
        
        if (count > 0) {
            return R.fail("该模块的同步策略已存在");
        }
        
        ShopSyncStrategy strategy = toEntity(dto);
        strategy.setStatus(1);
        strategy.setCreateTime(LocalDateTime.now());
        strategy.setUpdateTime(LocalDateTime.now());
        shopSyncStrategyMapper.insert(strategy);
        
        return R.ok(strategy.getId());
    }

    /**
     * 更新同步策略
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> updateStrategy(SyncConfigDTO dto) {
        if (dto.getId() == null) {
            return R.fail("策略 ID 不能为空");
        }
        
        ShopSyncStrategy existing = shopSyncStrategyMapper.selectById(dto.getId());
        if (existing == null) {
            return R.fail("策略不存在");
        }
        
        ShopSyncStrategy strategy = toEntity(dto);
        strategy.setUpdateTime(LocalDateTime.now());
        shopSyncStrategyMapper.updateById(strategy);
        
        return R.ok();
    }

    /**
     * 删除同步策略
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> deleteStrategy(Long id) {
        if (id == null) {
            return R.fail("策略 ID 不能为空");
        }
        
        shopSyncStrategyMapper.deleteById(id);
        return R.ok();
    }

    /**
     * 启用/禁用同步策略
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> updateStrategyStatus(Long id, Integer status) {
        if (id == null) {
            return R.fail("策略 ID 不能为空");
        }
        
        ShopSyncStrategy strategy = new ShopSyncStrategy();
        strategy.setId(id);
        strategy.setStatus(status);
        strategy.setUpdateTime(LocalDateTime.now());
        shopSyncStrategyMapper.updateById(strategy);
        
        return R.ok();
    }

    /**
     * 手动触发同步
     */
    public R<Void> manualSync(Long shopId, String module) {
        if (shopId == null || module == null) {
            return R.fail("参数不能为空");
        }
        
        // TODO: 触发实际的同步任务
        // 这里只是模拟，实际应该调用任务调度系统
        
        return R.ok();
    }

    // 转换方法
    private SyncConfigDTO toDTO(ShopSyncStrategy entity) {
        SyncConfigDTO dto = new SyncConfigDTO();
        dto.setId(entity.getId());
        dto.setShopId(entity.getShopId());
        dto.setSyncModule(entity.getSyncModule());
        dto.setSyncFrequency(entity.getSyncFrequency());
        dto.setCronExpression(entity.getCronExpression());
        dto.setStatus(entity.getStatus());
        dto.setLastSyncTime(entity.getLastSyncTime());
        dto.setNextSyncTime(entity.getNextSyncTime());
        return dto;
    }

    private ShopSyncStrategy toEntity(SyncConfigDTO dto) {
        ShopSyncStrategy entity = new ShopSyncStrategy();
        entity.setId(dto.getId());
        entity.setShopId(dto.getShopId());
        entity.setSyncModule(dto.getSyncModule());
        entity.setSyncFrequency(dto.getSyncFrequency());
        entity.setCronExpression(dto.getCronExpression());
        entity.setStatus(dto.getStatus());
        entity.setLastSyncTime(dto.getLastSyncTime());
        entity.setNextSyncTime(dto.getNextSyncTime());
        return entity;
    }
}
