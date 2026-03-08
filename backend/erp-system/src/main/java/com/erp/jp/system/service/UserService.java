package com.erp.jp.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.jp.common.exception.BusinessException;
import com.erp.jp.system.entity.User;
import com.erp.jp.system.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    
    /**
     * 创建用户
     */
    @Transactional(rollbackFor = Exception.class)
    public User createUser(User user) {
        // 校验用户名唯一性
        Long count = lambdaQuery()
            .eq(User::getUserName, user.getUserName())
            .count();
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        
        // 校验邮箱唯一性
        count = lambdaQuery()
            .eq(User::getEmail, user.getEmail())
            .count();
        if (count > 0) {
            throw new BusinessException("邮箱已被使用");
        }
        
        // TODO: 密码加密
        
        save(user);
        return user;
    }
    
    /**
     * 停用用户
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean disableUser(Long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setStatus(1); // 停用
        return updateById(user);
    }
}
