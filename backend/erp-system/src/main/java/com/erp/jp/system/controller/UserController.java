package com.erp.jp.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.jp.common.result.R;
import com.erp.jp.system.entity.User;
import com.erp.jp.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/v1/system/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 创建用户
     */
    @PostMapping
    public R<User> create(@RequestBody User user) {
        User saved = userService.createUser(user);
        return R.ok(saved);
    }
    
    /**
     * 分页查询用户列表
     */
    @GetMapping("/page")
    public R<Page<User>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<User> page = new Page<>(current, size);
        return R.ok(userService.page(page));
    }
    
    /**
     * 停用用户
     */
    @PutMapping("/{userId}/disable")
    public R<Void> disable(@PathVariable Long userId) {
        userService.disableUser(userId);
        return R.ok();
    }
}
