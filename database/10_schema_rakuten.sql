-- ============================================================
-- 日本跨境电商 ERP - 乐天店铺配置表
-- ============================================================
USE `erp_jp_crossborder`;

-- ----------------------------
-- 乐天店铺配置表
-- ----------------------------
DROP TABLE IF EXISTS `t_rakuten_shop`;
CREATE TABLE `t_rakuten_shop` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `store_id` BIGINT NOT NULL COMMENT '关联店铺 ID',
  `app_id` VARCHAR(100) NOT NULL COMMENT '乐天 App ID',
  `app_secret` VARCHAR(200) NOT NULL COMMENT 'App Secret(加密)',
  `access_token` TEXT DEFAULT NULL COMMENT '访问令牌 (加密)',
  `refresh_token` TEXT DEFAULT NULL COMMENT '刷新令牌 (加密)',
  `token_expire_time` DATETIME DEFAULT NULL COMMENT '令牌过期时间',
  `shop_name` VARCHAR(100) DEFAULT NULL COMMENT '乐天店铺名称',
  `shop_number` VARCHAR(50) DEFAULT NULL COMMENT '乐天店铺编号',
  `is_mock` TINYINT NOT NULL DEFAULT 0 COMMENT '是否模拟模式：0-真实 1-模拟',
  `sync_config` JSON DEFAULT NULL COMMENT '同步配置 JSON',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_store_id` (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='乐天店铺配置表';

-- ----------------------------
-- 乐天订单原始数据表
-- ----------------------------
DROP TABLE IF EXISTS `t_rakuten_order`;
CREATE TABLE `t_rakuten_order` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL COMMENT '租户 ID',
  `shop_id` BIGINT NOT NULL COMMENT '店铺 ID',
  `platform_order_id` VARCHAR(64) NOT NULL COMMENT '乐天订单 ID',
  `order_data` JSON NOT NULL COMMENT '原始订单数据 JSON',
  `sync_time` DATETIME NOT NULL COMMENT '同步时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_shop_order` (`shop_id`, `platform_order_id`),
  KEY `idx_sync_time` (`sync_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='乐天订单原始数据表';

-- ----------------------------
-- 乐天商品原始数据表
-- ----------------------------
DROP TABLE IF EXISTS `t_rakuten_product`;
CREATE TABLE `t_rakuten_product` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL COMMENT '租户 ID',
  `shop_id` BIGINT NOT NULL COMMENT '店铺 ID',
  `platform_product_id` VARCHAR(64) NOT NULL COMMENT '乐天商品 ID',
  `product_data` JSON NOT NULL COMMENT '原始商品数据 JSON',
  `sync_time` DATETIME NOT NULL COMMENT '同步时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_shop_product` (`shop_id`, `platform_product_id`),
  KEY `idx_sync_time` (`sync_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='乐天商品原始数据表';

-- ----------------------------
-- 乐天同步日志表
-- ----------------------------
DROP TABLE IF EXISTS `t_rakuten_sync_log`;
CREATE TABLE `t_rakuten_sync_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL COMMENT '租户 ID',
  `shop_id` BIGINT NOT NULL COMMENT '店铺 ID',
  `sync_type` VARCHAR(20) NOT NULL COMMENT '同步类型：ORDER/PRODUCT/INVENTORY',
  `sync_mode` VARCHAR(10) NOT NULL COMMENT '同步模式：FULL/INCREMENTAL',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-进行中 1-成功 2-失败',
  `fetch_count` INT DEFAULT 0 COMMENT '拉取数量',
  `success_count` INT DEFAULT 0 COMMENT '成功数量',
  `fail_count` INT DEFAULT 0 COMMENT '失败数量',
  `error_message` TEXT DEFAULT NULL COMMENT '错误信息',
  `raw_response` JSON DEFAULT NULL COMMENT '原始响应 JSON',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_shop_type` (`shop_id`, `sync_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='乐天同步日志表';
