package com.erp.jp.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.jp.common.exception.BusinessException;
import com.erp.jp.common.exception.ErrorCode;
import com.erp.jp.common.util.DateUtils;
import com.erp.jp.system.dto.TenantRegisterRequest;
import com.erp.jp.system.dto.TenantResponse;
import com.erp.jp.system.entity.Tenant;
import com.erp.jp.system.mapper.TenantMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 租户服务
 */
@Service
public class TenantService extends ServiceImpl<TenantMapper, Tenant> {
    
    /**
     * 注册新租户
     */
    @Transactional(rollbackFor = Exception.class)
    public TenantResponse registerTenant(TenantRegisterRequest request) {
        // 1. 校验企业名称唯一性
        Long count = lambdaQuery()
            .eq(Tenant::getTenantName, request.getTenantName())
            .count();
        if (count > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "企业名称已被注册");
        }
        
        // 2. 校验邮箱唯一性
        count = lambdaQuery()
            .eq(Tenant::getAdminEmail, request.getAdminEmail())
            .count();
        if (count > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "管理员邮箱已被注册");
        }
        
        // 3. 创建租户
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(request, tenant);
        tenant.setTenantCode(generateTenantCode());
        tenant.setStatus(1); // 正常
        tenant.setExpireTime(LocalDateTime.now().plusYears(1)); // 默认 1 年有效期
        
        save(tenant);
        
        // 4. TODO: 初始化租户数据（组织/角色/权限等）
        
        // 5. 返回响应
        TenantResponse response = new TenantResponse();
        BeanUtils.copyProperties(tenant, response);
        response.setTenantId(tenant.getId());
        response.setExpireTime(DateUtils.format(tenant.getExpireTime()));
        
        return response;
    }
    
    /**
     * 生成租户编码
     */
    private String generateTenantCode() {
        return "TEN_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
    
    /**
     * 根据 ID 查询租户
     */
    public TenantResponse getById(Long tenantId) {
        Tenant tenant = getById(tenantId);
        if (tenant == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "租户不存在");
        }
        
        TenantResponse response = new TenantResponse();
        BeanUtils.copyProperties(tenant, response);
        response.setTenantId(tenant.getId());
        response.setExpireTime(DateUtils.format(tenant.getExpireTime()));
        
        return response;
    }
}
