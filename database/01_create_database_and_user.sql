-- ============================================================
-- 日本跨境电商 ERP - 数据库与用户创建脚本
-- 执行前请确保 MySQL 已安装且 root 可登录
-- 执行方式: mysql -u root -p < 01_create_database_and_user.sql
-- ============================================================

-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS `erp_jp_crossborder`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- 2. 创建专用数据库用户（生产环境请修改密码）
CREATE USER IF NOT EXISTS 'erp_app'@'localhost' IDENTIFIED BY 'Erp_Jp_2026!';
CREATE USER IF NOT EXISTS 'erp_app'@'127.0.0.1' IDENTIFIED BY 'Erp_Jp_2026!';

-- 3. 授权（仅限 erp_jp_crossborder 库）
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, INDEX, ALTER, REFERENCES, CREATE VIEW, SHOW VIEW, TRIGGER, LOCK TABLES, EXECUTE
  ON `erp_jp_crossborder`.* TO 'erp_app'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, INDEX, ALTER, REFERENCES, CREATE VIEW, SHOW VIEW, TRIGGER, LOCK TABLES, EXECUTE
  ON `erp_jp_crossborder`.* TO 'erp_app'@'127.0.0.1';

FLUSH PRIVILEGES;

-- 4. 切换至新库
USE `erp_jp_crossborder`;

-- 验证
SELECT 'Database erp_jp_crossborder and user erp_app created successfully.' AS result;
