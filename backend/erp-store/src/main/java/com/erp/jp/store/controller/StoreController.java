package com.erp.jp.store.controller;

import com.erp.jp.common.result.R;
import com.erp.jp.store.dto.StoreCreateDTO;
import com.erp.jp.store.dto.StoreVO;
import com.erp.jp.store.entity.Warehouse;
import com.erp.jp.store.service.StoreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 店铺管理 API
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
     * 仓库列表
     */
    @GetMapping("/warehouses")
    public R<List<Warehouse>> listWarehouses(@RequestParam(required = false) Long tenantId) {
        return storeService.listWarehouses(tenantId);
    }
}
