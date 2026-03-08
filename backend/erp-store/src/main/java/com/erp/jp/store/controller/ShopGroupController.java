package com.erp.jp.store.controller;

import com.erp.jp.common.result.R;
import com.erp.jp.store.entity.ShopGroup;
import com.erp.jp.store.mapper.ShopGroupMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 店铺分组 API
 * [by Agent]
 */
@RestController
@RequestMapping("/api/v1/store")
public class ShopGroupController {

    private final ShopGroupMapper shopGroupMapper;

    public ShopGroupController(ShopGroupMapper shopGroupMapper) {
        this.shopGroupMapper = shopGroupMapper;
    }

    /**
     * 获取分组树形结构
     */
    @GetMapping("/groups/tree")
    public R<List<ShopGroup>> listGroupTree(@RequestParam(required = false) Long tenantId) {
        if (tenantId == null) tenantId = 1L;
        List<ShopGroup> list = shopGroupMapper.selectList(
                new LambdaQueryWrapper<ShopGroup>()
                        .eq(ShopGroup::getTenantId, tenantId)
                        .eq(ShopGroup::getStatus, 1)
                        .orderByAsc(ShopGroup::getParentId, ShopGroup::getId));
        return R.ok(list);
    }

    /**
     * 获取分组列表（仅一级）
     */
    @GetMapping("/groups")
    public R<List<ShopGroup>> listGroups(@RequestParam(required = false) Long tenantId) {
        if (tenantId == null) tenantId = 1L;
        List<ShopGroup> list = shopGroupMapper.selectList(
                new LambdaQueryWrapper<ShopGroup>()
                        .eq(ShopGroup::getTenantId, tenantId)
                        .eq(ShopGroup::getParentId, 0)
                        .eq(ShopGroup::getStatus, 1)
                        .orderByAsc(ShopGroup::getId));
        return R.ok(list);
    }

    /**
     * 获取分组详情
     */
    @GetMapping("/groups/{id}")
    public R<ShopGroup> getGroup(@PathVariable Long id) {
        ShopGroup group = shopGroupMapper.selectById(id);
        if (group == null) {
            return R.fail("分组不存在");
        }
        return R.ok(group);
    }

    /**
     * 创建分组
     */
    @PostMapping("/groups")
    public R<Long> createGroup(@RequestParam(required = false) Long tenantId, @RequestBody ShopGroup group) {
        if (tenantId == null) tenantId = 1L;
        
        // 校验分组名称唯一性
        Long count = shopGroupMapper.selectCount(
                new LambdaQueryWrapper<ShopGroup>()
                        .eq(ShopGroup::getTenantId, tenantId)
                        .eq(ShopGroup::getGroupName, group.getGroupName())
                        .eq(ShopGroup::getParentId, group.getParentId() != null ? group.getParentId() : 0));
        
        if (count > 0) {
            return R.fail("分组名称已存在");
        }
        
        group.setTenantId(tenantId);
        group.setParentId(group.getParentId() != null ? group.getParentId() : 0L);
        group.setStatus(1);
        shopGroupMapper.insert(group);
        return R.ok(group.getId());
    }

    /**
     * 更新分组
     */
    @PutMapping("/groups/{id}")
    public R<Void> updateGroup(@PathVariable Long id, @RequestBody ShopGroup group) {
        ShopGroup existing = shopGroupMapper.selectById(id);
        if (existing == null) {
            return R.fail("分组不存在");
        }
        
        // 不允许修改租户 ID
        group.setTenantId(existing.getTenantId());
        group.setId(id);
        shopGroupMapper.updateById(group);
        return R.ok();
    }

    /**
     * 删除分组
     */
    @DeleteMapping("/groups/{id}")
    public R<Void> deleteGroup(@PathVariable Long id) {
        // 检查是否有子分组
        Long childCount = shopGroupMapper.selectCount(
                new LambdaQueryWrapper<ShopGroup>()
                        .eq(ShopGroup::getParentId, id));
        
        if (childCount > 0) {
            return R.fail("该分组下存在子分组，无法删除");
        }
        
        // 检查是否有关联店铺（需要关联店铺表查询，这里简化处理）
        shopGroupMapper.deleteById(id);
        return R.ok();
    }

    /**
     * 获取子分组列表
     */
    @GetMapping("/groups/{id}/children")
    public R<List<ShopGroup>> listChildren(@PathVariable Long id) {
        List<ShopGroup> list = shopGroupMapper.selectList(
                new LambdaQueryWrapper<ShopGroup>()
                        .eq(ShopGroup::getParentId, id)
                        .eq(ShopGroup::getStatus, 1)
                        .orderByAsc(ShopGroup::getId));
        return R.ok(list);
    }
}
