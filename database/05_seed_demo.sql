-- 演示数据（可选执行）
USE `erp_jp_crossborder`;

-- 租户
INSERT IGNORE INTO `sys_tenant` (`id`, `tenant_code`, `tenant_name`, `plan_id`, `status`, `region_code`, `industry_type`) VALUES
(1, 'TENANT001', '星云跨境', NULL, 1, 'JP', 'B2C_B2B');

-- 组织架构（根：总经办，子级：运营中心、供应链中心、财务部、IT信息部）
INSERT IGNORE INTO `sys_organization` (`id`, `tenant_id`, `org_name_ja`, `org_name_en`, `org_code`, `parent_id`, `ancestors`, `org_type`, `sort_order`, `status`) VALUES
(1, 1, '总经办', 'HQ', 'HQ', 0, '0', 1, 0, 0),
(2, 1, '运营中心', 'OPS', 'OPS', 1, '0,1', 2, 1, 0),
(3, 1, '供应链中心', 'SCM', 'SCM', 1, '0,1', 2, 2, 0),
(4, 1, '财务部', 'FIN', 'FIN', 1, '0,1', 2, 3, 0),
(5, 1, 'IT信息部', 'IT', 'IT', 1, '0,1', 2, 4, 0),
(21, 1, '日本 Amazon 一部', 'AMZ1', 'AMZ1', 2, '0,1,2', 3, 1, 0),
(22, 1, '日本 Rakuten 二部', 'RAK2', 'RAK2', 2, '0,1,2', 3, 2, 0),
(31, 1, '采购部', 'PUR', 'PUR', 3, '0,1,3', 3, 1, 0),
(32, 1, '国内仓储部', 'WH-CN', 'WH-CN', 3, '0,1,3', 3, 2, 0);

-- 仓库
INSERT IGNORE INTO `t_warehouse` (`tenant_id`, `warehouse_name`, `warehouse_code`, `region_code`, `status`) VALUES
(1, 'FBA (NRT1)', 'FBA-NRT1', 'JP', 1),
(1, '大阪直发仓', 'OSAKA-01', 'JP', 1),
(1, '深圳一仓', 'SZ-01', 'CN', 1);
