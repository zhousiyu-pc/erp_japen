# System 系统管理模块

## 📦 模块说明

系统管理模块提供多租户架构下的基础管理能力，包括：
- 租户管理
- 组织架构管理
- 用户管理
- 角色权限管理

---

## 🔧 API 接口

### 租户管理

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/v1/system/tenant/register | 注册新租户 |
| GET | /api/v1/system/tenant/{tenantId} | 查询租户详情 |

### 组织管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/v1/system/org/tree | 获取组织树 |
| POST | /api/v1/system/org/create | 创建组织节点 |
| DELETE | /api/v1/system/org/{id} | 删除组织节点 |

### 用户管理

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/v1/system/user | 创建用户 |
| GET | /api/v1/system/user/page | 分页查询用户 |
| PUT | /api/v1/system/user/{userId}/disable | 停用用户 |

### 角色管理

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/v1/system/role | 创建角色 |
| GET | /api/v1/system/role/page | 分页查询角色 |

---

## 📋 数据库表

| 表名 | 说明 |
|------|------|
| sys_tenant | 租户主表 |
| sys_organization | 组织架构表 |
| sys_user | 用户表 |
| sys_role | 角色表 |
| sys_user_role | 用户角色关联表 |
| sys_role_shop | 角色店铺权限表 |

---

## 🚀 启动方式

```bash
cd backend/erp-system
mvn spring-boot:run
```

服务端口：8081

---

## 📝 开发日志

- 2026-03-08: 完成基础 CRUD 功能
  - Tenant 租户管理
  - Organization 组织管理
  - User 用户管理
  - Role 角色管理

---

*最后更新：2026-03-08*
