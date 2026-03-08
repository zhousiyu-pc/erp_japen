# 日本跨境电商 ERP - 后端服务

## 技术栈

- Spring Boot 3.2
- Spring Cloud 2023
- MyBatis-Plus
- MySQL 8
- RabbitMQ（待集成）
- Redis（待集成）

## 模块说明

| 模块 | 端口 | 说明 |
|------|------|------|
| erp-gateway | 8080 | API 网关 |
| erp-auth | 8083 | 认证授权 |
| erp-system | 8081 | 系统管理（租户、组织、用户、角色） |
| erp-product | 8082 | 商品中心 PIM |

## 启动前准备

1. **执行数据库脚本**（在项目根目录 `database/` 下）：
   ```bash
   mysql -u root -p < database/01_create_database_and_user.sql
   mysql -u root -p < database/02_schema_system.sql
   mysql -u root -p < database/03_schema_store.sql
   mysql -u root -p < database/04_schema_product.sql
   ```

2. **修改数据库连接**（如需要）：编辑各模块 `application.yml` 中的 `spring.datasource` 配置。

## 启动方式

```bash
# 编译
mvn clean install -DskipTests

# 启动各服务（按依赖顺序）
cd erp-system && mvn spring-boot:run
cd erp-product && mvn spring-boot:run
cd erp-gateway && mvn spring-boot:run
```

## API 前缀

- 网关统一入口: `http://localhost:8080`
- 系统管理: `/api/v1/system/**`
- 商品中心: `/api/v1/pim/**`
