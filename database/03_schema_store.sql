-- ============================================================
-- 日本跨境电商 ERP - 店铺与渠道管理表结构
-- ============================================================
USE `erp_jp_crossborder`;

-- ----------------------------
-- 1. 店铺主表
-- ----------------------------
DROP TABLE IF EXISTS `t_store_master`;
CREATE TABLE `t_store_master` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL,
  `store_name` VARCHAR(100) NOT NULL COMMENT '店铺名称',
  `platform_code` VARCHAR(20) NOT NULL COMMENT 'AMAZON_JP/RAKUTEN/YAHOO_JP',
  `seller_id` VARCHAR(100) NOT NULL COMMENT '平台卖家ID',
  `auth_status` TINYINT NOT NULL DEFAULT 0 COMMENT '1-正常 2-即将过期 3-已失效',
  `is_sandbox` TINYINT NOT NULL DEFAULT 0 COMMENT '0-生产 1-沙箱',
  `group_id` BIGINT DEFAULT NULL COMMENT '所属分组ID',
  `manager_id` BIGINT DEFAULT NULL COMMENT '负责人用户ID',
  `region_code` VARCHAR(10) DEFAULT 'JP',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-停用 1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_platform_seller` (`tenant_id`, `platform_code`, `seller_id`),
  KEY `idx_tenant` (`tenant_id`),
  KEY `idx_group` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺主表';

-- ----------------------------
-- 2. 店铺授权信息表(敏感字段加密存储)
-- ----------------------------
DROP TABLE IF EXISTS `t_store_auth_info`;
CREATE TABLE `t_store_auth_info` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `store_id` BIGINT NOT NULL,
  `api_key` VARCHAR(255) DEFAULT NULL COMMENT '加密存储',
  `api_secret` TEXT DEFAULT NULL COMMENT '加密存储',
  `access_token` TEXT DEFAULT NULL,
  `refresh_token` TEXT DEFAULT NULL,
  `token_expire_time` DATETIME DEFAULT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_store_id` (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺授权信息';

-- ----------------------------
-- 3. 店铺配置表
-- ----------------------------
DROP TABLE IF EXISTS `t_shop_config`;
CREATE TABLE `t_shop_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `shop_id` BIGINT NOT NULL,
  `tenant_id` BIGINT NOT NULL,
  `shop_alias` VARCHAR(64) NOT NULL COMMENT '店铺别名',
  `business_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1-B2C 2-B2B 3-B2B2C',
  `default_warehouse_id` BIGINT DEFAULT NULL,
  `currency` VARCHAR(10) NOT NULL DEFAULT 'JPY',
  `timezone` VARCHAR(50) DEFAULT 'Asia/Tokyo',
  `tax_config` JSON DEFAULT NULL COMMENT '税务配置JSON',
  `sync_strategy` JSON DEFAULT NULL COMMENT '同步策略JSON',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_shop_id` (`shop_id`),
  KEY `idx_tenant` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺配置表';

-- ----------------------------
-- 4. 店铺同步策略表
-- ----------------------------
DROP TABLE IF EXISTS `t_shop_sync_strategy`;
CREATE TABLE `t_shop_sync_strategy` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `shop_id` BIGINT NOT NULL,
  `tenant_id` BIGINT NOT NULL,
  `sync_module` VARCHAR(20) NOT NULL COMMENT 'ORDER/INVENTORY/PRODUCT',
  `sync_frequency` INT NOT NULL COMMENT '分钟',
  `cron_expression` VARCHAR(50) DEFAULT NULL,
  `config_json` JSON DEFAULT NULL,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-禁用 1-启用',
  `last_sync_time` DATETIME DEFAULT NULL,
  `next_sync_time` DATETIME DEFAULT NULL,
  `create_by` VARCHAR(50) DEFAULT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_shop_module` (`shop_id`, `sync_module`),
  KEY `idx_tenant` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺同步策略';

-- ----------------------------
-- 5. 仓库主表（店铺默认发货仓）
-- ----------------------------
DROP TABLE IF EXISTS `t_warehouse`;
CREATE TABLE `t_warehouse` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL,
  `warehouse_name` VARCHAR(100) NOT NULL COMMENT '仓库名称',
  `warehouse_code` VARCHAR(50) DEFAULT NULL,
  `region_code` VARCHAR(10) DEFAULT 'JP',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-停用 1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_tenant` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='仓库主表';

-- ----------------------------
-- 6. 店铺分组表
-- ----------------------------
DROP TABLE IF EXISTS `t_shop_group`;
CREATE TABLE `t_shop_group` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL,
  `group_name` VARCHAR(100) NOT NULL,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-禁用 1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_tenant_parent` (`tenant_id`, `parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺分组表';
