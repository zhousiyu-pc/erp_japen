package com.erp.jp.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.jp.system.entity.Role;
import com.erp.jp.system.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
 * 角色服务
 */
@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> {
}
