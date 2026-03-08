package com.erp.jp.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * 系统管理模块启动类
 */
@SpringBootApplication
@MapperScan("com.erp.jp.system.mapper")
@EntityScan("com.erp.jp.system.entity")
public class SystemApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }
}
