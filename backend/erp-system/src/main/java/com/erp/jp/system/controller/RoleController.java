package com.erp.jp.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.jp.common.result.R;
import com.erp.jp.system.entity.Role;
import com.erp.jp.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 角色控制器
 */
@RestController
@RequestMapping("/api/v1/system/role")
public class RoleController {
    
    @Autowired
    private RoleService roleService;
    
    /**
     * 创建角色
     */
    @PostMapping
    public R<Role> create(@RequestBody Role role) {
        roleService.save(role);
        return R.ok(role);
    }
    
    /**
     * 分页查询角色列表
     */
    @GetMapping("/page")
    public R<Page<Role>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Role> page = new Page<>(current, size);
        return R.ok(roleService.page(page));
    }
}
