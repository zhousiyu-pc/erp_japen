# 日本跨境电商 ERP - 数据库脚本

## 执行顺序

1. `01_create_database_and_user.sql` - 创建数据库和用户
2. `02_schema_system.sql` - 系统模块表（租户、组织、用户、角色、权限）
3. `03_schema_store.sql` - 店铺与渠道表
4. `04_schema_product.sql` - 商品中心(PIM)表

## 执行方式

```bash
# 方式一：使用 root 执行全部
mysql -u root -p < 01_create_database_and_user.sql
mysql -u root -p < 02_schema_system.sql
mysql -u root -p < 03_schema_store.sql
mysql -u root -p < 04_schema_product.sql

# 方式二：使用 erp_app 用户（需先执行 01）
mysql -u erp_app -p'Erp_Jp_2026!' erp_jp_crossborder < 02_schema_system.sql
```

## 连接信息（开发环境）

- 数据库: `erp_jp_crossborder`
- 用户: `erp_app`
- 密码: `Erp_Jp_2026!`（生产环境请修改）
- 字符集: utf8mb4
