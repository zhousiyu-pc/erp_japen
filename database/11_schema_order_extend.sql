-- ============================================================
-- 日本跨境电商 ERP - 订单物流表
-- ============================================================
USE `erp_jp_crossborder`;

-- ----------------------------
-- 订单物流表
-- ----------------------------
DROP TABLE IF EXISTS `t_order_shipping`;
CREATE TABLE `t_order_shipping` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT NOT NULL COMMENT '订单 ID',
  `order_no` VARCHAR(64) NOT NULL COMMENT '订单号',
  `shipping_company` VARCHAR(100) DEFAULT NULL COMMENT '物流公司',
  `shipping_no` VARCHAR(100) DEFAULT NULL COMMENT '物流单号',
  `shipping_status` TINYINT DEFAULT 0 COMMENT '物流状态：0-未发货 1-已发货 2-运输中 3-已签收 4-异常',
  `shipping_time` DATETIME DEFAULT NULL COMMENT '发货时间',
  `receive_time` DATETIME DEFAULT NULL COMMENT '签收时间',
  `shipping_address` VARCHAR(500) DEFAULT NULL COMMENT '收货地址',
  `shipping_name` VARCHAR(100) DEFAULT NULL COMMENT '收货人',
  `shipping_phone` VARCHAR(20) DEFAULT NULL COMMENT '收货人电话',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_order` (`order_id`),
  KEY `idx_shipping_no` (`shipping_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单物流表';

-- ----------------------------
-- 订单状态日志表
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
  KEY `idx_order` (`order_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单状态日志表';

-- ----------------------------
-- 库存同步记录表
-- ----------------------------
DROP TABLE IF EXISTS `t_inventory_sync`;
CREATE TABLE `t_inventory_sync` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL COMMENT '租户 ID',
  `shop_id` BIGINT NOT NULL COMMENT '店铺 ID',
  `sku_id` BIGINT NOT NULL COMMENT 'SKU ID',
  `sync_qty` INT NOT NULL COMMENT '同步数量',
  `sync_time` DATETIME NOT NULL COMMENT '同步时间',
  `sync_status` TINYINT NOT NULL DEFAULT 1 COMMENT '同步状态：1-成功 2-失败',
  `error_message` VARCHAR(500) DEFAULT NULL COMMENT '错误信息',
  `platform_response` JSON DEFAULT NULL COMMENT '平台响应 JSON',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_shop_sku` (`shop_id`, `sku_id`),
  KEY `idx_sync_time` (`sync_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存同步记录表';
