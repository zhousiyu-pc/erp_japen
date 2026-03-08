package com.erp.jp.system.controller;

import com.erp.jp.common.result.R;
import com.erp.jp.system.dto.OrgCreateDTO;
import com.erp.jp.system.dto.OrgTreeNode;
import com.erp.jp.system.service.OrgService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 组织管理 API - 组织结构 CRUD
 * 文档：账号与组织管理-组织结构配置
 */
@RestController
@RequestMapping("/api/v1/system/org")
public class OrgController {

    private final OrgService orgService;

    public OrgController(OrgService orgService) {
        this.orgService = orgService;
    }

    /**
     * 获取组织树
     */
    @GetMapping("/tree")
    public R<List<OrgTreeNode>> getOrgTree(@RequestParam(required = false) Long tenantId) {
        return orgService.getOrgTree(tenantId);
    }

    /**
     * 创建组织节点
     */
    @PostMapping("/create")
    public R<Long> createOrg(@RequestParam(required = false) Long tenantId, @RequestBody OrgCreateDTO dto) {
        return orgService.createOrg(tenantId, dto);
    }

    /**
     * 更新组织节点
     */
    @PutMapping("/update/{id}")
    public R<Void> updateOrg(@RequestParam(required = false) Long tenantId,
                             @PathVariable Long id,
                             @RequestBody OrgCreateDTO dto) {
        return orgService.updateOrg(tenantId, id, dto);
    }

    /**
     * 删除组织节点
     */
    @DeleteMapping("/{id}")
    public R<Void> deleteOrg(@RequestParam(required = false) Long tenantId, @PathVariable Long id) {
        return orgService.deleteOrg(tenantId, id);
    }
}
