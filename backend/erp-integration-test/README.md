# 日本跨境电商 ERP - 集成测试

基于 Testcontainers + Spring Boot Test 的 API 自动化测试工程。

## 运行方式

```bash
# 在 backend 目录下
mvn -pl erp-integration-test test

# 或仅运行指定测试类
mvn -pl erp-integration-test -Dtest=OrgApiIntegrationTest test
mvn -pl erp-integration-test -Dtest=StoreApiIntegrationTest test
```

## 前置要求

- Docker 已安装且运行中（Testcontainers 需要）
- JDK 17+

## 测试范围

| 模块 | 测试类 | 对应文档 |
|------|--------|----------|
| 组织结构 | OrgApiIntegrationTest | 账号与组织管理-组织结构 TC01~TC06 |
| 店铺管理 | StoreApiIntegrationTest | 店铺与渠道管理 |

## 测试用例说明

### 组织结构 (OrgApiIntegrationTest)

- **TC01**: 获取组织树，应返回种子数据树结构
- **TC02**: 新增根组织「本社」，应成功创建
- **TC03**: 新增子组织「楽天事業部」，应在总经办下创建
- **TC04**: 组织编码唯一性 - 重复编码 HQ 应失败
- **TC05**: 删除含子组织的节点 - 禁止删除
- **TC06**: 删除叶子节点 - 应成功
- **更新**: 更新组织名称 - 应成功

### 店铺 (StoreApiIntegrationTest)

- 店铺列表、创建店铺、仓库列表、分组列表
