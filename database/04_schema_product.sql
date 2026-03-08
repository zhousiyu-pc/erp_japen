-- ============================================================
-- 日本跨境电商 ERP - 商品中心(PIM)表结构
-- ============================================================
USE `erp_jp_crossborder`;

-- ----------------------------
-- 1. 商品SPU主表
-- ----------------------------
DROP TABLE IF EXISTS `pim_product_spu`;
CREATE TABLE `pim_product_spu` (
  `spu_id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL,
  `spu_code` VARCHAR(64) NOT NULL COMMENT 'SPU编码',
  `product_name` VARCHAR(500) DEFAULT NULL COMMENT '商品名称',
  `category_id` BIGINT DEFAULT NULL COMMENT '类目ID',
  `brand_id` BIGINT DEFAULT NULL COMMENT '品牌ID',
  `base_price` DECIMAL(15,2) DEFAULT NULL COMMENT '基础售价',
  `currency` VARCHAR(10) DEFAULT 'JPY',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-草稿 1-在售 2-停售',
  `create_by` VARCHAR(64) DEFAULT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`spu_id`),
  UNIQUE KEY `uk_tenant_spu` (`tenant_id`, `spu_code`),
  KEY `idx_tenant_category` (`tenant_id`, `category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品SPU主表';

-- ----------------------------
-- 2. 商品SKU表
-- ----------------------------
DROP TABLE IF EXISTS `pim_product_sku`;
CREATE TABLE `pim_product_sku` (
  `sku_id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL,
  `spu_id` BIGINT NOT NULL,
  `sku_code` VARCHAR(64) NOT NULL COMMENT 'SKU编码',
  `jan_code` VARCHAR(20) DEFAULT NULL COMMENT 'JAN码(日本商品编码)',
  `cost_price` DECIMAL(15,2) DEFAULT NULL,
  `sale_price` DECIMAL(15,2) DEFAULT NULL,
  `weight` DECIMAL(10,2) DEFAULT NULL COMMENT '重量(g)',
  `variant_attrs` JSON DEFAULT NULL COMMENT '变体属性JSON如{"color":"黑","size":"M"}',
  `status` TINYINT NOT NULL DEFAULT 1,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`sku_id`),
  UNIQUE KEY `uk_tenant_sku` (`tenant_id`, `sku_code`),
  KEY `idx_spu` (`spu_id`),
  KEY `idx_jan` (`jan_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品SKU表';

-- ----------------------------
-- 3. 商品多语言文案表
-- ----------------------------
DROP TABLE IF EXISTS `pim_product_localized`;
CREATE TABLE `pim_product_localized` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL,
  `spu_id` BIGINT NOT NULL,
  `locale` VARCHAR(10) NOT NULL COMMENT 'ja_JP/zh_CN/en_US',
  `title` VARCHAR(500) DEFAULT NULL,
  `subtitle` VARCHAR(500) DEFAULT NULL,
  `bullet_1` VARCHAR(1000) DEFAULT NULL,
  `bullet_2` VARCHAR(1000) DEFAULT NULL,
  `bullet_3` VARCHAR(1000) DEFAULT NULL,
  `bullet_4` VARCHAR(1000) DEFAULT NULL,
  `bullet_5` VARCHAR(1000) DEFAULT NULL,
  `description` TEXT DEFAULT NULL,
  `search_terms` VARCHAR(500) DEFAULT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_spu_locale` (`spu_id`, `locale`),
  KEY `idx_tenant` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品多语言文案';

-- ----------------------------
-- 4. 店铺商品映射表(Listing)
-- ----------------------------
DROP TABLE IF EXISTS `pim_product_listing`;
CREATE TABLE `pim_product_listing` (
  `listing_id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL,
  `spu_id` BIGINT NOT NULL,
  `sku_id` BIGINT NOT NULL,
  `shop_id` BIGINT NOT NULL,
  `platform_sku_id` VARCHAR(100) DEFAULT NULL COMMENT '平台SKU',
  `platform_product_id` VARCHAR(100) DEFAULT NULL COMMENT 'ASIN等',
  `sync_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-待同步 1-同步中 2-成功 3-失败',
  `last_sync_time` DATETIME DEFAULT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`listing_id`),
  UNIQUE KEY `uk_shop_sku` (`shop_id`, `sku_id`),
  KEY `idx_spu_shop` (`spu_id`, `shop_id`),
  KEY `idx_tenant` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺商品映射';

-- ----------------------------
-- 5. 分发任务表
-- ----------------------------
DROP TABLE IF EXISTS `sys_distribute_task`;
CREATE TABLE `sys_distribute_task` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `task_id` VARCHAR(64) NOT NULL COMMENT '任务唯一ID',
  `tenant_id` BIGINT NOT NULL,
  `task_type` VARCHAR(20) NOT NULL COMMENT 'BATCH_UPDATE/DISTRIBUTE',
  `total_count` INT NOT NULL DEFAULT 0,
  `success_count` INT NOT NULL DEFAULT 0,
  `fail_count` INT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-待执行 1-执行中 2-完成 3-失败',
  `error_log_url` VARCHAR(500) DEFAULT NULL,
  `create_by` VARCHAR(64) DEFAULT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_task_id` (`task_id`),
  KEY `idx_tenant_time` (`tenant_id`, `create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分发任务表';
