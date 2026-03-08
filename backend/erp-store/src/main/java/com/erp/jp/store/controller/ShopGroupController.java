package com.erp.jp.store.controller;

import com.erp.jp.common.result.R;
import com.erp.jp.store.entity.ShopGroup;
import com.erp.jp.store.mapper.ShopGroupMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 店铺分组 API
 */
@RestController
@RequestMapping("/api/v1/store")
public class ShopGroupController {

    private final ShopGroupMapper shopGroupMapper;

    public ShopGroupController(ShopGroupMapper shopGroupMapper) {
        this.shopGroupMapper = shopGroupMapper;
    }

    @GetMapping("/groups")
    public R<List<ShopGroup>> listGroups(@RequestParam(required = false) Long tenantId) {
        if (tenantId == null) tenantId = 1L;
        List<ShopGroup> list = shopGroupMapper.selectList(
                new LambdaQueryWrapper<ShopGroup>()
                        .eq(ShopGroup::getTenantId, tenantId)
                        .eq(ShopGroup::getParentId, 0)
                        .orderByAsc(ShopGroup::getId));
        return R.ok(list);
    }

    @PostMapping("/groups")
    public R<Long> createGroup(@RequestParam(required = false) Long tenantId, @RequestBody ShopGroup group) {
        if (tenantId == null) tenantId = 1L;
        group.setTenantId(tenantId);
        group.setParentId(group.getParentId() != null ? group.getParentId() : 0L);
        group.setStatus(1);
        shopGroupMapper.insert(group);
        return R.ok(group.getId());
    }

    @DeleteMapping("/groups/{id}")
    public R<Void> deleteGroup(@PathVariable Long id) {
        shopGroupMapper.deleteById(id);
        return R.ok();
    }
}
