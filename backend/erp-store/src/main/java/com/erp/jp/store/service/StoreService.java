package com.erp.jp.store.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.erp.jp.common.result.R;
import com.erp.jp.store.dto.StoreCreateDTO;
import com.erp.jp.store.dto.StoreVO;
import com.erp.jp.store.entity.*;
import com.erp.jp.store.mapper.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 店铺管理服务
 */
@Service
public class StoreService {

    private final StoreMasterMapper storeMasterMapper;
    private final StoreAuthInfoMapper storeAuthInfoMapper;
    private final ShopConfigMapper shopConfigMapper;
    private final WarehouseMapper warehouseMapper;

    public StoreService(StoreMasterMapper storeMasterMapper, StoreAuthInfoMapper storeAuthInfoMapper,
                        ShopConfigMapper shopConfigMapper, WarehouseMapper warehouseMapper) {
        this.storeMasterMapper = storeMasterMapper;
        this.storeAuthInfoMapper = storeAuthInfoMapper;
        this.shopConfigMapper = shopConfigMapper;
        this.warehouseMapper = warehouseMapper;
    }

    private static final Map<String, String> PLATFORM_DISPLAY = Map.of(
            "RAKUTEN", "Rakuten",
            "AMAZON_JP", "Amazon",
            "YAHOO_JP", "Yahoo"
    );

    /**
     * 获取店铺列表（含授权信息）
     */
    public R<List<StoreVO>> listStores(Long tenantId, String platformCode) {
        if (tenantId == null) tenantId = 1L;  // TODO: 从登录上下文获取
        List<StoreMaster> stores = storeMasterMapper.selectStoreListWithAuth(tenantId, platformCode);
        if (stores == null || stores.isEmpty()) {
            return R.ok(new ArrayList<>());
        }

        List<Long> storeIds = stores.stream().map(StoreMaster::getId).toList();
        List<StoreAuthInfo> authList = storeAuthInfoMapper.selectList(
                new LambdaQueryWrapper<StoreAuthInfo>().in(StoreAuthInfo::getStoreId, storeIds)
        );
        Map<Long, StoreAuthInfo> authMap = authList.stream().collect(Collectors.toMap(StoreAuthInfo::getStoreId, a -> a));

        List<ShopConfig> configs = shopConfigMapper.selectList(
                new LambdaQueryWrapper<ShopConfig>().in(ShopConfig::getShopId, storeIds)
        );
        Map<Long, ShopConfig> configMap = configs.stream().collect(Collectors.toMap(ShopConfig::getShopId, c -> c));

        List<Long> whIds = configs.stream().map(ShopConfig::getDefaultWarehouseId).filter(id -> id != null).distinct().toList();
        Map<Long, String> whNameMap = whIds.isEmpty() ? Map.of() : warehouseMapper.selectBatchIds(whIds).stream()
                .collect(Collectors.toMap(Warehouse::getId, Warehouse::getWarehouseName));

        List<StoreVO> result = new ArrayList<>();
        for (StoreMaster s : stores) {
            StoreVO vo = toVO(s, authMap.get(s.getId()), configMap.get(s.getId()), whNameMap);
            result.add(vo);
        }
        return R.ok(result);
    }

    private StoreVO toVO(StoreMaster s, StoreAuthInfo auth, ShopConfig config, Map<Long, String> whNameMap) {
        StoreVO vo = new StoreVO();
        vo.setId(s.getId());
        vo.setStoreName(s.getStoreName());
        vo.setPlatformCode(s.getPlatformCode());
        vo.setPlatform(PLATFORM_DISPLAY.getOrDefault(s.getPlatformCode(), s.getPlatformCode()));
        vo.setSellerId(s.getSellerId());
        vo.setAuthStatus(s.getAuthStatus());
        vo.setIsSandbox(s.getIsSandbox() != null && s.getIsSandbox() == 1);
        vo.setCreateTime(s.getCreateTime());

        if (config != null) {
            vo.setCurrency(config.getCurrency() != null ? config.getCurrency() : "JPY");
            vo.setTimezone(config.getTimezone() != null ? config.getTimezone() : "Asia/Tokyo");
            if (config.getDefaultWarehouseId() != null) {
                vo.setDefaultWarehouse(whNameMap.getOrDefault(config.getDefaultWarehouseId(), ""));
            }
        } else {
            vo.setCurrency("JPY");
            vo.setTimezone("Asia/Tokyo");
        }

        vo.setManager(s.getManagerId() != null ? "用户" + s.getManagerId() : "");

        if (auth != null && auth.getTokenExpireTime() != null) {
            LocalDate expire = auth.getTokenExpireTime().toLocalDate();
            vo.setExpireDate(expire);
            vo.setDaysLeft((int) ChronoUnit.DAYS.between(LocalDate.now(), expire));
            if (vo.getDaysLeft() > 15) vo.setStatus("正常");
            else if (vo.getDaysLeft() > 0) vo.setStatus("即将过期");
            else vo.setStatus("已失效");
        } else {
            vo.setStatus("未授权");
            vo.setDaysLeft(0);
        }
        return vo;
    }

    /**
     * 接入新店铺
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Long> createStore(Long tenantId, StoreCreateDTO dto) {
        if (tenantId == null) tenantId = 1L;
        StoreMaster store = new StoreMaster();
        store.setTenantId(tenantId);
        store.setStoreName(dto.getStoreName());
        store.setPlatformCode(dto.getPlatformCode());
        store.setSellerId(dto.getSellerId());
        store.setManagerId(dto.getManagerId());
        store.setIsSandbox(dto.getIsSandbox() != null && dto.getIsSandbox() ? 1 : 0);
        store.setAuthStatus(3);  // 待授权
        store.setStatus(1);
        store.setRegionCode("JP");
        storeMasterMapper.insert(store);

        StoreAuthInfo auth = new StoreAuthInfo();
        auth.setStoreId(store.getId());
        auth.setApiKey(dto.getApiKey());
        auth.setApiSecret(dto.getApiSecret());
        auth.setTokenExpireTime(LocalDateTime.now().plusDays(365));
        storeAuthInfoMapper.insert(auth);

        ShopConfig config = new ShopConfig();
        config.setShopId(store.getId());
        config.setTenantId(tenantId);
        config.setShopAlias(dto.getStoreName());
        config.setCurrency("JPY");
        config.setTimezone("Asia/Tokyo");
        config.setDefaultWarehouseId(dto.getDefaultWarehouseId());
        config.setBusinessType(1);
        shopConfigMapper.insert(config);

        return R.ok(store.getId());
    }

    /**
     * 解绑店铺
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> unbindStore(Long storeId) {
        storeMasterMapper.deleteById(storeId);
        storeAuthInfoMapper.delete(new LambdaQueryWrapper<StoreAuthInfo>().eq(StoreAuthInfo::getStoreId, storeId));
        shopConfigMapper.delete(new LambdaQueryWrapper<ShopConfig>().eq(ShopConfig::getShopId, storeId));
        return R.ok();
    }

    /**
     * 获取授权 URL
     */
    public R<String> getAuthUrl(Long storeId) {
        StoreMaster store = storeMasterMapper.selectById(storeId);
        if (store == null) return R.fail("店铺不存在");
        StoreAuthInfo auth = storeAuthInfoMapper.selectOne(
                new LambdaQueryWrapper<StoreAuthInfo>().eq(StoreAuthInfo::getStoreId, storeId));
        if (auth == null || auth.getApiKey() == null) return R.fail("请先配置 API Key");
        String url = "https://auth.rakuten.co.jp/authorize?client_id=" + auth.getApiKey()
                + "&redirect_uri=https://erp.example.com/callback&response_type=code";
        return R.ok(url);
    }

    /**
     * 获取仓库列表
     */
    public R<List<Warehouse>> listWarehouses(Long tenantId) {
        if (tenantId == null) tenantId = 1L;
        List<Warehouse> list = warehouseMapper.selectList(
                new LambdaQueryWrapper<Warehouse>().eq(Warehouse::getTenantId, tenantId).eq(Warehouse::getStatus, 1));
        return R.ok(list);
    }

    /**
     * 处理 OAuth 回调
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Map<String, Object>> handleOAuthCallback(String code, String state) {
        // TODO: 实现 OAuth 回调逻辑
        // 1. 解析 state 获取店铺 ID
        // 2. 使用 code 换取 access_token
        // 3. 更新店铺授权信息
        // 4. 触发首次数据同步
        
        return R.ok(Map.of("status", "success", "message", "授权成功"));
    }

    /**
     * 刷新店铺 Token
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Void> refreshToken(Long storeId) {
        if (storeId == null) {
            return R.fail("店铺 ID 不能为空");
        }
        
        StoreAuthInfo auth = storeAuthInfoMapper.selectOne(
                new LambdaQueryWrapper<StoreAuthInfo>().eq(StoreAuthInfo::getStoreId, storeId));
        
        if (auth == null) {
            return R.fail("店铺授权信息不存在");
        }
        
        // TODO: 调用平台 API 刷新 Token
        // auth.setAccessToken(newAccessToken);
        // auth.setRefreshToken(newRefreshToken);
        // auth.setTokenExpireTime(LocalDateTime.now().plusDays(30));
        
        return R.ok();
    }

    /**
     * 验证店铺连接
     */
    public R<Void> verifyStore(Long storeId) {
        if (storeId == null) {
            return R.fail("店铺 ID 不能为空");
        }
        
        StoreMaster store = storeMasterMapper.selectById(storeId);
        if (store == null) {
            return R.fail("店铺不存在");
        }
        
        // TODO: 调用平台 API 验证连接
        // 例如：调用 GetServiceStatus 接口
        
        return R.ok();
    }
}
