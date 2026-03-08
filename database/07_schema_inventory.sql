-- ============================================================
-- 日本跨境电商 ERP - 库存管理表结构
-- ============================================================
USE `erp_jp_crossborder`;

-- ----------------------------
-- 1. 库存主表（四态模型）
-- ----------------------------
DROP TABLE IF EXISTS `t_inventory`;
CREATE TABLE `t_inventory` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL COMMENT '租户 ID',
  `warehouse_id` BIGINT NOT NULL COMMENT '仓库 ID',
  `product_id` BIGINT DEFAULT NULL COMMENT '商品 ID',
  `sku_id` BIGINT NOT NULL COMMENT 'SKU ID',
  `qty_physical` INT NOT NULL DEFAULT 0 COMMENT '实物库存',
  `qty_available` INT NOT NULL DEFAULT 0 COMMENT '可用库存',
  `qty_locked` INT NOT NULL DEFAULT 0 COMMENT '锁定库存（已下单未发货）',
  `qty_intransit` INT NOT NULL DEFAULT 0 COMMENT '在途库存（采购中）',
  `batch_no` VARCHAR(50) DEFAULT NULL COMMENT '批次号',
  `production_date` DATETIME DEFAULT NULL COMMENT '生产日期',
  `expiry_date` DATETIME DEFAULT NULL COMMENT '有效期至',
  `location_code` VARCHAR(50) DEFAULT NULL COMMENT '库位编码',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_warehouse_sku_batch` (`warehouse_id`, `sku_id`, `batch_no`),
  KEY `idx_sku` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存主表';

-- ----------------------------
-- 2. 库存流水表
-- ----------------------------
DROP TABLE IF EXISTS `t_inventory_transaction`;
CREATE TABLE `t_inventory_transaction` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL COMMENT '租户 ID',
  `warehouse_id` BIGINT NOT NULL COMMENT '仓库 ID',
  `sku_id` BIGINT NOT NULL COMMENT 'SKU ID',
  `batch_no` VARCHAR(50) DEFAULT NULL COMMENT '批次号',
  `trans_type` VARCHAR(20) NOT NULL COMMENT '变动类型：IN-入库 OUT-出库 LOCK-锁定 UNLOCK-解锁 TRANSFER-调拨',
  `qty_change` INT NOT NULL COMMENT '变动数量（正数增加，负数减少）',
  `qty_physical_before` INT NOT NULL DEFAULT 0 COMMENT '变动前实物库存',
  `qty_physical_after` INT NOT NULL DEFAULT 0 COMMENT '变动后实物库存',
  `qty_available_before` INT NOT NULL DEFAULT 0 COMMENT '变动前可用库存',
  `qty_available_after` INT NOT NULL DEFAULT 0 COMMENT '变动后可用库存',
  `ref_no` VARCHAR(64) DEFAULT NULL COMMENT '关联单据号（采购单/订单/调拨单）',
  `operator` VARCHAR(50) DEFAULT NULL COMMENT '操作人',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_warehouse_sku` (`warehouse_id`, `sku_id`),
  KEY `idx_ref_no` (`ref_no`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存流水表';

-- ----------------------------
-- 3. 库存预警表
-- ----------------------------
DROP TABLE IF EXISTS `t_inventory_warning`;
CREATE TABLE `t_inventory_warning` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL COMMENT '租户 ID',
  `warehouse_id` BIGINT NOT NULL COMMENT '仓库 ID',
  `sku_id` BIGINT NOT NULL COMMENT 'SKU ID',
  `warning_type` TINYINT NOT NULL COMMENT '预警类型：1-库存不足 2-库存积压 3-效期预警',
  `current_qty` INT NOT NULL COMMENT '当前库存',
  `threshold_qty` INT NOT NULL COMMENT '阈值',
  `warning_level` TINYINT NOT NULL DEFAULT 1 COMMENT '预警级别：1-低 2-中 3-高',
  `is_handled` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已处理',
  `handler` VARCHAR(50) DEFAULT NULL COMMENT '处理人',
  `handle_time` DATETIME DEFAULT NULL COMMENT '处理时间',
  `handle_remark` VARCHAR(500) DEFAULT NULL COMMENT '处理备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sku` (`sku_id`),
  KEY `idx_handled` (`is_handled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存预警表';
