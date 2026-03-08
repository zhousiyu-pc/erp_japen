package com.erp.jp.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.jp.system.entity.Tenant;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租户 Mapper
 */
@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {
}
