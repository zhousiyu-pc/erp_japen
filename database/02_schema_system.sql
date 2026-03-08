-- ============================================================
-- 日本跨境电商 ERP - 系统模块表结构 (账号与组织、权限)
-- 执行前请先执行 01_create_database_and_user.sql
-- ============================================================
USE `erp_jp_crossborder`;

-- ----------------------------
-- 1. 租户主表
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_code` VARCHAR(32) NOT NULL COMMENT '租户编码，全局唯一',
  `tenant_name` VARCHAR(100) NOT NULL COMMENT '企业名称',
  `plan_id` BIGINT DEFAULT NULL COMMENT '关联套餐ID',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用, 1-正常, 2-欠费锁定',
  `expire_time` DATETIME DEFAULT NULL COMMENT '服务到期时间',
  `db_schema` VARCHAR(50) DEFAULT NULL COMMENT '预留：物理隔离schema',
  `tax_id` VARCHAR(20) DEFAULT NULL COMMENT '纳税识别号(日本法人番号等)',
  `region_code` VARCHAR(10) DEFAULT 'JP' COMMENT '目标市场区域',
  `industry_type` VARCHAR(20) DEFAULT 'B2C_B2B' COMMENT '业务模式：B2C/B2B/B2C_B2B',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_code` (`tenant_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户主表';

-- ----------------------------
-- 2. 租户配额表
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_quota`;
CREATE TABLE `sys_tenant_quota` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL,
  `resource_type` VARCHAR(20) NOT NULL COMMENT 'USER/SHOP/SKU',
  `limit_num` INT NOT NULL DEFAULT 0,
  `used_num` INT NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_resource` (`tenant_id`, `resource_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户配额表';

-- ----------------------------
-- 3. 组织架构表
-- ----------------------------
DROP TABLE IF EXISTS `sys_organization`;
CREATE TABLE `sys_organization` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL,
  `org_name_ja` VARCHAR(100) DEFAULT NULL COMMENT '组织日文名称',
  `org_name_en` VARCHAR(100) DEFAULT NULL COMMENT '组织英文名称',
  `org_code` VARCHAR(50) NOT NULL COMMENT '组织编码',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父节点ID，根为0',
  `ancestors` VARCHAR(500) DEFAULT NULL COMMENT '祖级列表，逗号分隔',
  `leader_id` BIGINT DEFAULT NULL COMMENT '负责人用户ID',
  `org_type` TINYINT DEFAULT 2 COMMENT '1-公司, 2-部门, 3-小组',
  `sort_order` INT DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-正常 1-停用',
  `create_by` VARCHAR(64) DEFAULT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_tenant_parent` (`tenant_id`, `parent_id`),
  KEY `idx_tenant_code` (`tenant_id`, `org_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组织架构表';

-- ----------------------------
-- 4. 角色表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL,
  `role_name` VARCHAR(64) NOT NULL,
  `role_code` VARCHAR(64) DEFAULT NULL,
  `data_scope` TINYINT NOT NULL DEFAULT 1 COMMENT '1-全部 2-本部门及子 3-本部门 4-仅本人 5-自定义',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-正常 1-停用',
  `del_flag` TINYINT NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`role_id`),
  KEY `idx_tenant` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- ----------------------------
-- 5. 用户表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL,
  `dept_id` BIGINT DEFAULT NULL COMMENT '部门ID',
  `user_name` VARCHAR(30) NOT NULL COMMENT '登录账号',
  `real_name` VARCHAR(64) DEFAULT NULL COMMENT '真实姓名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码(BCrypt)',
  `email` VARCHAR(50) DEFAULT NULL,
  `phone` VARCHAR(20) DEFAULT NULL,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-正常 1-停用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_tenant_username` (`tenant_id`, `user_name`),
  KEY `idx_tenant_dept` (`tenant_id`, `dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ----------------------------
-- 6. 用户角色关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联';

-- ----------------------------
-- 7. 角色菜单关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` BIGINT NOT NULL,
  `menu_id` BIGINT NOT NULL,
  PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单权限';

-- ----------------------------
-- 8. 角色店铺数据权限表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_shop`;
CREATE TABLE `sys_role_shop` (
  `role_id` BIGINT NOT NULL,
  `shop_id` BIGINT NOT NULL,
  PRIMARY KEY (`role_id`, `shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色店铺数据权限';

-- ----------------------------
-- 9. 角色仓库数据权限表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_warehouse`;
CREATE TABLE `sys_role_warehouse` (
  `role_id` BIGINT NOT NULL,
  `warehouse_id` BIGINT NOT NULL,
  PRIMARY KEY (`role_id`, `warehouse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色仓库数据权限';

-- ----------------------------
-- 10. 菜单权限表
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` BIGINT NOT NULL AUTO_INCREMENT,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `menu_name` VARCHAR(50) NOT NULL,
  `menu_type` CHAR(1) NOT NULL COMMENT 'M-目录 C-菜单 F-按钮',
  `perms` VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
  `path` VARCHAR(200) DEFAULT NULL,
  `component` VARCHAR(200) DEFAULT NULL,
  `sort_order` INT DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';

-- ----------------------------
-- 11. 操作审计日志表
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` VARCHAR(32) NOT NULL,
  `user_id` BIGINT NOT NULL,
  `username` VARCHAR(128) DEFAULT NULL,
  `event_type` VARCHAR(32) NOT NULL COMMENT 'LOGIN/LOGOUT/ROLE_UPDATE/DATA_EXPORT等',
  `event_desc` VARCHAR(500) DEFAULT NULL,
  `ip_address` VARCHAR(64) DEFAULT NULL,
  `user_agent` VARCHAR(256) DEFAULT NULL,
  `source_module` VARCHAR(32) DEFAULT NULL,
  `target_resource` VARCHAR(256) DEFAULT NULL,
  `before_value` JSON DEFAULT NULL,
  `after_value` JSON DEFAULT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_tenant_time` (`tenant_id`, `create_time`),
  KEY `idx_user_time` (`user_id`, `create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作审计日志';
