package com.erp.jp.store.controller;

import com.erp.jp.common.result.R;
import com.erp.jp.store.dto.StoreCreateDTO;
import com.erp.jp.store.dto.StoreVO;
import com.erp.jp.store.entity.Warehouse;
import com.erp.jp.store.service.StoreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 店铺管理 API
 * [by Agent]
 */
@RestController
@RequestMapping("/api/v1/store")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    /**
     * 店铺列表
     */
    @GetMapping("/stores")
    public R<List<StoreVO>> listStores(
            @RequestParam(required = false) Long tenantId,
            @RequestParam(required = false, defaultValue = "all") String platformCode) {
        return storeService.listStores(tenantId, platformCode);
    }

    /**
     * 接入新店铺
     */
    @PostMapping("/stores")
    public R<Long> createStore(@RequestParam(required = false) Long tenantId, @RequestBody StoreCreateDTO dto) {
        return storeService.createStore(tenantId, dto);
    }

    /**
     * 解绑店铺
     */
    @DeleteMapping("/stores/{id}")
    public R<Void> unbindStore(@PathVariable Long id) {
        return storeService.unbindStore(id);
    }

    /**
     * 获取授权 URL
     */
    @GetMapping("/stores/{id}/auth-url")
    public R<String> getAuthUrl(@PathVariable Long id) {
        return storeService.getAuthUrl(id);
    }

    /**
     * OAuth 回调处理
     */
    @GetMapping("/oauth/callback")
    public R<Map<String, Object>> oauthCallback(
            @RequestParam String code,
            @RequestParam String state) {
        return storeService.handleOAuthCallback(code, state);
    }

    /**
     * 刷新店铺 Token
     */
    @PostMapping("/stores/{id}/refresh-token")
    public R<Void> refreshToken(@PathVariable Long id) {
        return storeService.refreshToken(id);
    }

    /**
     * 验证店铺连接
     */
    @PostMapping("/stores/{id}/verify")
    public R<Void> verifyStore(@PathVariable Long id) {
        return storeService.verifyStore(id);
    }

    /**
     * 仓库列表
     */
    @GetMapping("/warehouses")
    public R<List<Warehouse>> listWarehouses(@RequestParam(required = false) Long tenantId) {
        return storeService.listWarehouses(tenantId);
    }
}
