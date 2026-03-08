package com.erp.jp.system.controller;

import com.erp.jp.common.result.R;
import com.erp.jp.system.entity.Organization;
import com.erp.jp.system.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 组织控制器
 */
@RestController
@RequestMapping("/api/v1/system/org")
public class OrganizationController {
    
    @Autowired
    private OrganizationService organizationService;
    
    /**
     * 获取组织树
     */
    @GetMapping("/tree")
    public R<List<Organization>> listTree(@RequestParam Long tenantId) {
        List<Organization> list = organizationService.listTree(tenantId);
        return R.ok(list);
    }
    
    /**
     * 创建组织节点
     */
    @PostMapping("/create")
    public R<Organization> create(@RequestBody Organization org) {
        Organization saved = organizationService.createOrg(org);
        return R.ok(saved);
    }
    
    /**
     * 删除组织节点
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        organizationService.deleteById(id);
        return R.ok();
    }
}
