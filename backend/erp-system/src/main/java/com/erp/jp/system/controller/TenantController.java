package com.erp.jp.system.controller;

import com.erp.jp.common.result.R;
import com.erp.jp.system.dto.TenantRegisterRequest;
import com.erp.jp.system.dto.TenantResponse;
import com.erp.jp.system.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 租户控制器
 */
@RestController
@RequestMapping("/api/v1/system/tenant")
public class TenantController {
    
    @Autowired
    private TenantService tenantService;
    
    /**
     * 注册新租户
     */
    @PostMapping("/register")
    public R<TenantResponse> register(@RequestBody @Validated TenantRegisterRequest request) {
        TenantResponse response = tenantService.registerTenant(request);
        return R.ok(response);
    }
    
    /**
     * 查询租户详情
     */
    @GetMapping("/{tenantId}")
    public R<TenantResponse> getById(@PathVariable Long tenantId) {
        TenantResponse response = tenantService.getById(tenantId);
        return R.ok(response);
    }
}
