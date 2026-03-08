-- ============================================================
-- 日本跨境电商 ERP - 订单管理表结构
-- ============================================================
USE `erp_jp_crossborder`;

-- ----------------------------
-- 1. 订单主表
-- ----------------------------
DROP TABLE IF EXISTS `t_order_master`;
CREATE TABLE `t_order_master` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_no` VARCHAR(64) NOT NULL COMMENT '订单号',
  `tenant_id` BIGINT NOT NULL COMMENT '租户 ID',
  `shop_id` BIGINT NOT NULL COMMENT '店铺 ID',
  `platform_code` VARCHAR(20) NOT NULL COMMENT '平台编码：RAKUTEN/AMAZON_JP/YAHOO_JP',
  `platform_order_id` VARCHAR(64) NOT NULL COMMENT '平台订单 ID',
  `order_status` TINYINT NOT NULL DEFAULT 10 COMMENT '订单状态：10-待审核 20-已审核 30-已发货 40-已完成 0-已取消 50-售后中',
  `order_type` TINYINT NOT NULL DEFAULT 1 COMMENT '订单类型：1-B2C 2-B2B',
  `order_time` DATETIME NOT NULL COMMENT '下单时间',
  `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
  `ship_time` DATETIME DEFAULT NULL COMMENT '发货时间',
  `complete_time` DATETIME DEFAULT NULL COMMENT '完成时间',
  
  -- 买家信息
  `buyer_name` VARCHAR(100) DEFAULT NULL COMMENT '买家姓名',
  `buyer_email` VARCHAR(100) DEFAULT NULL COMMENT '买家邮箱',
  `buyer_phone` VARCHAR(20) DEFAULT NULL COMMENT '买家电话',
  
  -- 收货地址
  `shipping_name` VARCHAR(100) DEFAULT NULL COMMENT '收货人姓名',
  `shipping_phone` VARCHAR(20) DEFAULT NULL COMMENT '收货人电话',
  `shipping_province` VARCHAR(50) DEFAULT NULL COMMENT '都道府县',
  `shipping_city` VARCHAR(50) DEFAULT NULL COMMENT '市区町村',
  `shipping_district` VARCHAR(50) DEFAULT NULL COMMENT '区/街道',
  `shipping_address` VARCHAR(500) DEFAULT NULL COMMENT '详细地址',
  `shipping_zip` VARCHAR(10) DEFAULT NULL COMMENT '邮编',
  
  -- 金额信息
  `total_amount` DECIMAL(10,2) NOT NULL COMMENT '订单总额（JPY）',
  `pay_amount` DECIMAL(10,2) NOT NULL COMMENT '实付金额（JPY）',
  `freight_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '运费（JPY）',
  `discount_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '优惠金额（JPY）',
  `currency` VARCHAR(10) DEFAULT 'JPY' COMMENT '币种',
  
  -- 扩展信息
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `platform_remark` VARCHAR(500) DEFAULT NULL COMMENT '平台备注',
  `raw_data` JSON DEFAULT NULL COMMENT '原始数据 JSON',
  
  -- 系统字段
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_platform_order` (`tenant_id`, `platform_code`, `platform_order_id`),
  KEY `idx_shop_time` (`shop_id`, `order_time`),
  KEY `idx_status` (`order_status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单主表';

-- ----------------------------
-- 2. 订单明细表
-- ----------------------------
DROP TABLE IF EXISTS `t_order_item`;
CREATE TABLE `t_order_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT NOT NULL COMMENT '订单 ID',
  `order_no` VARCHAR(64) NOT NULL COMMENT '订单号',
  `product_id` BIGINT DEFAULT NULL COMMENT '商品 ID',
  `sku_id` BIGINT DEFAULT NULL COMMENT 'SKU ID',
  `platform_product_id` VARCHAR(64) DEFAULT NULL COMMENT '平台商品 ID',
  `platform_sku_id` VARCHAR(64) DEFAULT NULL COMMENT '平台 SKU ID',
  `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称',
  `product_image` VARCHAR(500) DEFAULT NULL COMMENT '商品图片',
  `sku_spec` VARCHAR(200) DEFAULT NULL COMMENT 'SKU 规格',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '数量',
  `unit_price` DECIMAL(10,2) NOT NULL COMMENT '单价（JPY）',
  `total_price` DECIMAL(10,2) NOT NULL COMMENT '总价（JPY）',
  `currency` VARCHAR(10) DEFAULT 'JPY' COMMENT '币种',
  PRIMARY KEY (`id`),
  KEY `idx_order` (`order_id`),
  KEY `idx_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单明细表';

-- ----------------------------
-- 3. 订单状态日志表
-- ----------------------------
DROP TABLE IF EXISTS `t_order_status_log`;
CREATE TABLE `t_order_status_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT NOT NULL COMMENT '订单 ID',
  `from_status` TINYINT DEFAULT NULL COMMENT '原状态',
  `to_status` TINYINT NOT NULL COMMENT '新状态',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `operator` VARCHAR(50) DEFAULT NULL COMMENT '操作人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单状态日志表';
