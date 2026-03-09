-- ============================================================
-- 日本跨境电商 ERP - 任务调度表结构
-- ============================================================
USE `erp_jp_crossborder`;

-- ----------------------------
-- 1. 任务执行日志表
-- ----------------------------
DROP TABLE IF EXISTS `t_job_execution_log`;
CREATE TABLE `t_job_execution_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `job_name` VARCHAR(100) NOT NULL COMMENT '任务名称',
  `job_type` VARCHAR(50) NOT NULL COMMENT '任务类型：ORDER_SYNC/INVENTORY_SYNC/PRODUCT_SYNC',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '执行状态：0-执行中 1-成功 2-失败',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
  `fetch_count` INT DEFAULT 0 COMMENT '拉取数量',
  `success_count` INT DEFAULT 0 COMMENT '成功数量',
  `fail_count` INT DEFAULT 0 COMMENT '失败数量',
  `error_message` TEXT DEFAULT NULL COMMENT '错误信息',
  `raw_response` JSON DEFAULT NULL COMMENT '原始响应 JSON',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_job_type` (`job_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务执行日志表';

-- ----------------------------
-- 2. 任务配置表
-- ----------------------------
DROP TABLE IF EXISTS `t_job_config`;
CREATE TABLE `t_job_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `job_name` VARCHAR(100) NOT NULL COMMENT '任务名称',
  `job_type` VARCHAR(50) NOT NULL COMMENT '任务类型',
  `cron_expression` VARCHAR(50) DEFAULT NULL COMMENT 'Cron 表达式',
  `fixed_rate` BIGINT DEFAULT NULL COMMENT '固定间隔（毫秒）',
  `is_enabled` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用：0-禁用 1-启用',
  `last_execution_time` DATETIME DEFAULT NULL COMMENT '最后执行时间',
  `next_execution_time` DATETIME DEFAULT NULL COMMENT '下次执行时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_job_type` (`job_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务配置表';

-- 初始化任务配置
INSERT INTO `t_job_config` (`job_name`, `job_type`, `fixed_rate`, `is_enabled`, `remark`) VALUES
('乐天订单同步', 'ORDER_SYNC', 900000, 1, '每 15 分钟同步一次乐天订单'),
('乐天库存同步', 'INVENTORY_SYNC', 1800000, 1, '每 30 分钟同步一次库存'),
('乐天商品同步', 'PRODUCT_SYNC', 3600000, 1, '每小时同步一次商品');
