package com.erp.jp.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.erp.jp.common.result.R;
import com.erp.jp.system.dto.OrgCreateDTO;
import com.erp.jp.system.dto.OrgTreeNode;
import com.erp.jp.system.entity.SysOrganization;
import com.erp.jp.system.mapper.SysOrganizationMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 组织管理服务 - 组织结构 CRUD 及树形构建
 */
@Service
public class OrgService {

    private final SysOrganizationMapper orgMapper;
    private static final int MAX_DEPTH = 5;

    public OrgService(SysOrganizationMapper orgMapper) {
        this.orgMapper = orgMapper;
    }

    /**
     * 获取组织树
     */
    public R<List<OrgTreeNode>> getOrgTree(Long tenantId) {
        if (tenantId == null) {
            tenantId = 1L; // 默认租户
        }
        List<SysOrganization> list = orgMapper.selectList(
                new LambdaQueryWrapper<SysOrganization>()
                        .eq(SysOrganization::getTenantId, tenantId)
                        .orderByAsc(SysOrganization::getSortOrder, SysOrganization::getId)
        );
        List<OrgTreeNode> tree = buildTree(list, 0L, 0);
        return R.ok(tree);
    }

    /**
     * 创建组织节点
     */
    public R<Long> createOrg(Long tenantId, OrgCreateDTO dto) {
        if (tenantId == null) tenantId = 1L;

        // 唯一性校验
        long sameCode = orgMapper.selectCount(
                new LambdaQueryWrapper<SysOrganization>()
                        .eq(SysOrganization::getTenantId, tenantId)
                        .eq(SysOrganization::getOrgCode, dto.getOrgCode())
        );
        if (sameCode > 0) {
            return R.fail(400, "组织编码已存在");
        }

        Long parentId = dto.getParentOrgId() != null ? dto.getParentOrgId() : 0L;
        String ancestors = "0";
        if (parentId > 0) {
            SysOrganization parent = orgMapper.selectById(parentId);
            if (parent == null || !tenantId.equals(parent.getTenantId())) {
                return R.fail(400, "父级组织不存在");
            }
            int depth = (parent.getAncestors() != null ? parent.getAncestors().split(",").length : 0) + 1;
            if (depth >= MAX_DEPTH) {
                return R.fail(400, "组织层级不可超过5级");
            }
            ancestors = parent.getAncestors() != null ? parent.getAncestors() + "," + parentId : String.valueOf(parentId);
        }

        SysOrganization org = new SysOrganization();
        org.setTenantId(tenantId);
        org.setOrgNameJa(dto.getOrgNameJa() != null ? dto.getOrgNameJa() : "");
        org.setOrgNameEn(dto.getOrgNameEn());
        org.setOrgCode(dto.getOrgCode());
        org.setParentId(parentId);
        org.setAncestors(ancestors);
        org.setLeaderId(dto.getLeaderId());
        org.setOrgType(2); // 默认部门
        org.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        org.setStatus(dto.getStatus() != null ? dto.getStatus() : 0);
        orgMapper.insert(org);
        return R.ok(org.getId());
    }

    /**
     * 更新组织节点
     */
    public R<Void> updateOrg(Long tenantId, Long id, OrgCreateDTO dto) {
        if (tenantId == null) tenantId = 1L;
        SysOrganization org = orgMapper.selectById(id);
        if (org == null || !tenantId.equals(org.getTenantId())) {
            return R.fail(404, "组织不存在");
        }
        if (dto.getOrgNameJa() != null) org.setOrgNameJa(dto.getOrgNameJa());
        if (dto.getOrgNameEn() != null) org.setOrgNameEn(dto.getOrgNameEn());
        if (dto.getOrgCode() != null) {
            long sameCode = orgMapper.selectCount(
                    new LambdaQueryWrapper<SysOrganization>()
                            .eq(SysOrganization::getTenantId, tenantId)
                            .eq(SysOrganization::getOrgCode, dto.getOrgCode())
                            .ne(SysOrganization::getId, id)
            );
            if (sameCode > 0) return R.fail(400, "组织编码已存在");
            org.setOrgCode(dto.getOrgCode());
        }
        if (dto.getLeaderId() != null) org.setLeaderId(dto.getLeaderId());
        if (dto.getSortOrder() != null) org.setSortOrder(dto.getSortOrder());
        if (dto.getStatus() != null) org.setStatus(dto.getStatus());
        orgMapper.updateById(org);
        return R.ok();
    }

    /**
     * 删除组织节点
     */
    public R<Void> deleteOrg(Long tenantId, Long id) {
        if (tenantId == null) tenantId = 1L;
        SysOrganization org = orgMapper.selectById(id);
        if (org == null || !tenantId.equals(org.getTenantId())) {
            return R.fail(404, "组织不存在");
        }
        long childCount = orgMapper.selectCount(
                new LambdaQueryWrapper<SysOrganization>()
                        .eq(SysOrganization::getParentId, id)
        );
        if (childCount > 0) {
            return R.fail(400, "该组织下存在子组织，禁止删除");
        }
        orgMapper.deleteById(id);
        return R.ok();
    }

    private List<OrgTreeNode> buildTree(List<SysOrganization> list, Long parentId, int depth) {
        if (depth >= MAX_DEPTH) return new ArrayList<>();
        List<OrgTreeNode> result = new ArrayList<>();
        for (SysOrganization o : list) {
            Long p = o.getParentId() == null ? 0L : o.getParentId();
            if (java.util.Objects.equals(p, parentId)) {
                OrgTreeNode node = new OrgTreeNode();
                node.setId(o.getId());
                String label = (o.getOrgNameJa() != null && !o.getOrgNameJa().isEmpty())
                        ? o.getOrgNameJa()
                        : (o.getOrgNameEn() != null ? o.getOrgNameEn() : o.getOrgCode());
                node.setLabel(label);
                node.setOrgCode(o.getOrgCode());
                node.setParentId(o.getParentId());
                node.setStatus(o.getStatus());
                node.setChildren(buildTree(list, o.getId(), depth + 1));
                result.add(node);
            }
        }
        return result;
    }
}
