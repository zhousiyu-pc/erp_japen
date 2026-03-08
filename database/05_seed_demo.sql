-- 演示数据（可选执行）
USE `erp_jp_crossborder`;

-- 仓库
INSERT IGNORE INTO `t_warehouse` (`tenant_id`, `warehouse_name`, `warehouse_code`, `region_code`, `status`) VALUES
(1, 'FBA (NRT1)', 'FBA-NRT1', 'JP', 1),
(1, '大阪直发仓', 'OSAKA-01', 'JP', 1),
(1, '深圳一仓', 'SZ-01', 'CN', 1);
