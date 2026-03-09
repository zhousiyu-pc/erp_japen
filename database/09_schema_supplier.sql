-- ============================================================
-- 日本跨境电商 ERP - 采购管理表结构
-- ============================================================
USE `erp_jp_crossborder`;

-- ----------------------------
-- 1. 供应商主表
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier`;
CREATE TABLE `t_supplier` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL COMMENT '租户 ID',
  `supplier_code` VARCHAR(50) NOT NULL COMMENT '供应商编码',
  `supplier_name` VARCHAR(100) NOT NULL COMMENT '供应商名称',
  `supplier_type` TINYINT DEFAULT 1 COMMENT '供应商类型：1-工厂 2-贸易商 3-个人',
  `country_code` VARCHAR(10) DEFAULT 'CN' COMMENT '国家代码',
  `settlement_currency` VARCHAR(10) DEFAULT 'CNY' COMMENT '结算币种',
  `invoice_registration_number` VARCHAR(20) DEFAULT NULL COMMENT '日本发票注册号',
  `lead_time_days` INT DEFAULT 7 COMMENT '采购提前期（天）',
  `rating_score` DECIMAL(5,2) DEFAULT 0 COMMENT '综合评分',
  `rating_level` CHAR(1) DEFAULT 'B' COMMENT '评级等级：S/A/B/C/D',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_supplier_code` (`supplier_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='供应商主表';

-- ----------------------------
-- 2. 采购单主表
-- ----------------------------
DROP TABLE IF EXISTS `t_purchase_order`;
CREATE TABLE `t_purchase_order` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_no` VARCHAR(64) NOT NULL COMMENT '采购单号',
  `tenant_id` BIGINT NOT NULL COMMENT '租户 ID',
  `supplier_id` BIGINT NOT NULL COMMENT '供应商 ID',
  `warehouse_id` BIGINT NOT NULL COMMENT '仓库 ID',
  `currency` VARCHAR(10) NOT NULL DEFAULT 'CNY' COMMENT '采购币种',
  `exchange_rate` DECIMAL(18,6) DEFAULT NULL COMMENT '汇率（原币->JPY）',
  `total_amount_origin` DECIMAL(18,2) DEFAULT 0 COMMENT '原币总金额',
  `total_amount_jpy` DECIMAL(18,2) DEFAULT 0 COMMENT 'JPY 总金额',
  `order_status` TINYINT NOT NULL DEFAULT 10 COMMENT '订单状态：10-待审核 20-已审核 30-部分到货 40-已完成 50-已取消',
  `estimated_arrival_date` DATETIME DEFAULT NULL COMMENT '预计到货日期',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_supplier` (`supplier_id`),
  KEY `idx_status` (`order_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购单主表';

-- ----------------------------
-- 3. 采购单明细表
-- ----------------------------
DROP TABLE IF EXISTS `t_purchase_order_item`;
CREATE TABLE `t_purchase_order_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT NOT NULL COMMENT '采购单 ID',
  `sku_id` BIGINT NOT NULL COMMENT 'SKU ID',
  `quantity` INT NOT NULL COMMENT '采购数量',
  `unit_price_origin` DECIMAL(18,4) NOT NULL COMMENT '原币单价',
  `unit_price_jpy` DECIMAL(18,4) DEFAULT NULL COMMENT 'JPY 单价',
  `total_price_origin` DECIMAL(18,2) DEFAULT NULL COMMENT '原币总价',
  `received_qty` INT DEFAULT 0 COMMENT '已收货数量',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_order` (`order_id`),
  KEY `idx_sku` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购单明细表';
