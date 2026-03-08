package com.erp.jp.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.jp.common.exception.BusinessException;
import com.erp.jp.system.entity.Organization;
import com.erp.jp.system.mapper.OrganizationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 组织服务
 */
@Service
public class OrganizationService extends ServiceImpl<OrganizationMapper, Organization> {
    
    /**
     * 创建组织节点
     */
    @Transactional(rollbackFor = Exception.class)
    public Organization createOrg(Organization org) {
        // 校验组织编码唯一性
        Long count = lambdaQuery()
            .eq(Organization::getOrgCode, org.getOrgCode())
            .count();
        if (count > 0) {
            throw new BusinessException("组织编码已存在");
        }
        
        // 构建祖级列表
        if (org.getParentId() != null && org.getParentId() > 0) {
            Organization parent = getById(org.getParentId());
            if (parent != null) {
                org.setAncestors(parent.getAncestors() + "," + parent.getId());
            }
        } else {
            org.setAncestors("0");
        }
        
        save(org);
        return org;
    }
    
    /**
     * 获取组织树
     */
    public List<Organization> listTree(Long tenantId) {
        return lambdaQuery()
            .eq(Organization::getTenantId, tenantId)
            .orderByAsc(Organization::getSort)
            .list();
    }
    
    /**
     * 删除组织节点
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long orgId) {
        // 检查是否有子节点
        Long childCount = lambdaQuery()
            .eq(Organization::getParentId, orgId)
            .count();
        if (childCount > 0) {
            throw new BusinessException("该组织下存在子节点，禁止删除");
        }
        
        return removeById(orgId);
    }
}
