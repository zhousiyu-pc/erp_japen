-- 集成测试初始化 - 创建数据库及表结构
CREATE DATABASE IF NOT EXISTS `erp_jp_crossborder` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `erp_jp_crossborder`;

-- sys_tenant
DROP TABLE IF EXISTS `sys_oper_log`;
DROP TABLE IF EXISTS `sys_menu`;
DROP TABLE IF EXISTS `sys_role_warehouse`;
DROP TABLE IF EXISTS `sys_role_shop`;
DROP TABLE IF EXISTS `sys_role_menu`;
DROP TABLE IF EXISTS `sys_user_role`;
DROP TABLE IF EXISTS `sys_user`;
DROP TABLE IF EXISTS `sys_role`;
DROP TABLE IF EXISTS `sys_organization`;
DROP TABLE IF EXISTS `sys_tenant_quota`;
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant` (
  `id` BIGINT NOT NULL AUTO_INCREMENT, `tenant_code` VARCHAR(32) NOT NULL, `tenant_name` VARCHAR(100) NOT NULL,
  `plan_id` BIGINT DEFAULT NULL, `status` TINYINT NOT NULL DEFAULT 1, `expire_time` DATETIME DEFAULT NULL,
  `db_schema` VARCHAR(50) DEFAULT NULL, `tax_id` VARCHAR(20) DEFAULT NULL, `region_code` VARCHAR(10) DEFAULT 'JP',
  `industry_type` VARCHAR(20) DEFAULT 'B2C_B2B', `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, `is_deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`), UNIQUE KEY `uk_tenant_code` (`tenant_code`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `sys_tenant_quota` (`id` BIGINT NOT NULL AUTO_INCREMENT, `tenant_id` BIGINT NOT NULL, `resource_type` VARCHAR(20) NOT NULL,
  `limit_num` INT NOT NULL DEFAULT 0, `used_num` INT NOT NULL DEFAULT 0, `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_resource` (`tenant_id`, `resource_type`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `sys_organization` (
  `id` BIGINT NOT NULL AUTO_INCREMENT, `tenant_id` BIGINT NOT NULL, `org_name_ja` VARCHAR(100) DEFAULT NULL,
  `org_name_en` VARCHAR(100) DEFAULT NULL, `org_code` VARCHAR(50) NOT NULL, `parent_id` BIGINT NOT NULL DEFAULT 0,
  `ancestors` VARCHAR(500) DEFAULT NULL, `leader_id` BIGINT DEFAULT NULL, `org_type` TINYINT DEFAULT 2,
  `sort_order` INT DEFAULT 0, `status` TINYINT NOT NULL DEFAULT 0, `create_by` VARCHAR(64) DEFAULT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT NOT NULL DEFAULT 0, PRIMARY KEY (`id`), KEY `idx_tenant_parent` (`tenant_id`, `parent_id`),
  KEY `idx_tenant_code` (`tenant_id`, `org_code`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `sys_role` (`role_id` BIGINT NOT NULL AUTO_INCREMENT, `tenant_id` BIGINT NOT NULL, `role_name` VARCHAR(64) NOT NULL,
  `role_code` VARCHAR(64) DEFAULT NULL, `data_scope` TINYINT NOT NULL DEFAULT 1, `status` TINYINT NOT NULL DEFAULT 0,
  `del_flag` TINYINT NOT NULL DEFAULT 0, `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY (`role_id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `sys_user` (`user_id` BIGINT NOT NULL AUTO_INCREMENT, `tenant_id` BIGINT NOT NULL, `dept_id` BIGINT DEFAULT NULL,
  `user_name` VARCHAR(30) NOT NULL, `real_name` VARCHAR(64) DEFAULT NULL, `password` VARCHAR(100) NOT NULL,
  `email` VARCHAR(50) DEFAULT NULL, `phone` VARCHAR(20) DEFAULT NULL, `status` TINYINT NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT NOT NULL DEFAULT 0, PRIMARY KEY (`user_id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `sys_user_role` (`user_id` BIGINT NOT NULL, `role_id` BIGINT NOT NULL, PRIMARY KEY (`user_id`, `role_id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `sys_role_menu` (`role_id` BIGINT NOT NULL, `menu_id` BIGINT NOT NULL, PRIMARY KEY (`role_id`, `menu_id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `sys_role_shop` (`role_id` BIGINT NOT NULL, `shop_id` BIGINT NOT NULL, PRIMARY KEY (`role_id`, `shop_id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `sys_role_warehouse` (`role_id` BIGINT NOT NULL, `warehouse_id` BIGINT NOT NULL, PRIMARY KEY (`role_id`, `warehouse_id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `sys_menu` (`menu_id` BIGINT NOT NULL AUTO_INCREMENT, `parent_id` BIGINT NOT NULL DEFAULT 0, `menu_name` VARCHAR(50) NOT NULL,
  `menu_type` CHAR(1) NOT NULL, `perms` VARCHAR(100) DEFAULT NULL, `path` VARCHAR(200) DEFAULT NULL, `component` VARCHAR(200) DEFAULT NULL,
  `sort_order` INT DEFAULT 0, `status` TINYINT NOT NULL DEFAULT 0, `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY (`menu_id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `sys_oper_log` (`id` BIGINT NOT NULL AUTO_INCREMENT, `tenant_id` VARCHAR(32) NOT NULL, `user_id` BIGINT NOT NULL,
  `username` VARCHAR(128) DEFAULT NULL, `event_type` VARCHAR(32) NOT NULL, `event_desc` VARCHAR(500) DEFAULT NULL,
  `ip_address` VARCHAR(64) DEFAULT NULL, `user_agent` VARCHAR(256) DEFAULT NULL, `source_module` VARCHAR(32) DEFAULT NULL,
  `target_resource` VARCHAR(256) DEFAULT NULL, `before_value` JSON DEFAULT NULL, `after_value` JSON DEFAULT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- store tables
DROP TABLE IF EXISTS `t_shop_group`;
DROP TABLE IF EXISTS `t_warehouse`;
DROP TABLE IF EXISTS `t_shop_sync_strategy`;
DROP TABLE IF EXISTS `t_shop_config`;
DROP TABLE IF EXISTS `t_store_auth_info`;
DROP TABLE IF EXISTS `t_store_master`;
CREATE TABLE `t_store_master` (`id` BIGINT NOT NULL AUTO_INCREMENT, `tenant_id` BIGINT NOT NULL, `store_name` VARCHAR(100) NOT NULL,
  `platform_code` VARCHAR(20) NOT NULL, `seller_id` VARCHAR(100) NOT NULL, `auth_status` TINYINT NOT NULL DEFAULT 0,
  `is_sandbox` TINYINT NOT NULL DEFAULT 0, `group_id` BIGINT DEFAULT NULL, `manager_id` BIGINT DEFAULT NULL,
  `region_code` VARCHAR(10) DEFAULT 'JP', `status` TINYINT NOT NULL DEFAULT 1, `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, `is_deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`), UNIQUE KEY `uk_tenant_platform_seller` (`tenant_id`, `platform_code`, `seller_id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `t_store_auth_info` (`id` BIGINT NOT NULL AUTO_INCREMENT, `store_id` BIGINT NOT NULL, `api_key` VARCHAR(255) DEFAULT NULL,
  `api_secret` TEXT DEFAULT NULL, `access_token` TEXT DEFAULT NULL, `refresh_token` TEXT DEFAULT NULL, `token_expire_time` DATETIME DEFAULT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`), UNIQUE KEY `uk_store_id` (`store_id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `t_shop_config` (`id` BIGINT NOT NULL AUTO_INCREMENT, `shop_id` BIGINT NOT NULL, `tenant_id` BIGINT NOT NULL,
  `shop_alias` VARCHAR(64) NOT NULL, `business_type` TINYINT NOT NULL DEFAULT 1, `default_warehouse_id` BIGINT DEFAULT NULL,
  `currency` VARCHAR(10) NOT NULL DEFAULT 'JPY', `timezone` VARCHAR(50) DEFAULT 'Asia/Tokyo', `tax_config` JSON DEFAULT NULL, `sync_strategy` JSON DEFAULT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT NOT NULL DEFAULT 0, PRIMARY KEY (`id`), UNIQUE KEY `uk_shop_id` (`shop_id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `t_shop_sync_strategy` (`id` BIGINT NOT NULL AUTO_INCREMENT, `shop_id` BIGINT NOT NULL, `tenant_id` BIGINT NOT NULL,
  `sync_module` VARCHAR(20) NOT NULL, `sync_frequency` INT NOT NULL, `cron_expression` VARCHAR(50) DEFAULT NULL, `config_json` JSON DEFAULT NULL,
  `status` TINYINT NOT NULL DEFAULT 1, `last_sync_time` DATETIME DEFAULT NULL, `next_sync_time` DATETIME DEFAULT NULL, `create_by` VARCHAR(50) DEFAULT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`), UNIQUE KEY `uk_shop_module` (`shop_id`, `sync_module`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `t_warehouse` (`id` BIGINT NOT NULL AUTO_INCREMENT, `tenant_id` BIGINT NOT NULL, `warehouse_name` VARCHAR(100) NOT NULL,
  `warehouse_code` VARCHAR(50) DEFAULT NULL, `region_code` VARCHAR(10) DEFAULT 'JP', `status` TINYINT NOT NULL DEFAULT 1,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT NOT NULL DEFAULT 0, PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `t_shop_group` (`id` BIGINT NOT NULL AUTO_INCREMENT, `tenant_id` BIGINT NOT NULL, `group_name` VARCHAR(100) NOT NULL,
  `parent_id` BIGINT NOT NULL DEFAULT 0, `status` TINYINT NOT NULL DEFAULT 1, `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, `is_deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- seed
INSERT INTO `sys_tenant` (`id`, `tenant_code`, `tenant_name`, `plan_id`, `status`, `region_code`, `industry_type`) VALUES
(1, 'TENANT001', '星云跨境', NULL, 1, 'JP', 'B2C_B2B');
INSERT INTO `sys_organization` (`id`, `tenant_id`, `org_name_ja`, `org_name_en`, `org_code`, `parent_id`, `ancestors`, `org_type`, `sort_order`, `status`) VALUES
(1, 1, '总经办', 'HQ', 'HQ', 0, '0', 1, 0, 0),
(2, 1, '运营中心', 'OPS', 'OPS', 1, '0,1', 2, 1, 0),
(3, 1, '供应链中心', 'SCM', 'SCM', 1, '0,1', 2, 2, 0),
(4, 1, '财务部', 'FIN', 'FIN', 1, '0,1', 2, 3, 0),
(5, 1, 'IT信息部', 'IT', 'IT', 1, '0,1', 2, 4, 0),
(21, 1, '日本 Amazon 一部', 'AMZ1', 'AMZ1', 2, '0,1,2', 3, 1, 0),
(22, 1, '日本 Rakuten 二部', 'RAK2', 'RAK2', 2, '0,1,2', 3, 2, 0);
INSERT INTO `t_warehouse` (`tenant_id`, `warehouse_name`, `warehouse_code`, `region_code`, `status`) VALUES
(1, 'FBA (NRT1)', 'FBA-NRT1', 'JP', 1),
(1, '大阪直发仓', 'OSAKA-01', 'JP', 1);
