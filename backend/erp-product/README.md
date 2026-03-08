# ERP Japan - Product Module

## 📦 商品中心（PIM）模块

### 当前进度

#### ✅ 已完成
- [x] 数据库表结构设计（5 张表）
- [x] Entity 实体类（3 个）
  - ProductSpu
  - ProductSku
  - ProductLocalized
- [x] Mapper 数据访问层（3 个）
- [x] Service 业务逻辑层（3 个）
- [x] Controller API 接口
- [x] DTO 请求/响应对象

#### ⏳ 待完成
- [ ] 单元测试（覆盖率>80%）
- [ ] ProductListing 店铺映射功能
- [ ] 分发任务功能
- [ ] API 文档（Swagger/OpenAPI）
- [ ] 集成测试

---

## 🔧 API 接口列表

### SPU 管理
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/v1/product/spu | 创建商品 SPU |
| GET | /api/v1/product/spu/{spuId} | 查询 SPU 详情 |
| GET | /api/v1/product/spu/page | 分页查询 SPU 列表 |
| PUT | /api/v1/product/spu/{spuId}/status | 更新 SPU 状态 |
| DELETE | /api/v1/product/spu/{spuId} | 删除 SPU |

### SKU 管理
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/v1/product/sku | 创建商品 SKU |
| POST | /api/v1/product/sku/batch | 批量创建 SKU |
| GET | /api/v1/product/sku/spu/{spuId} | 查询 SPU 下所有 SKU |

### 多语言文案
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/v1/product/localized | 保存/更新多语言文案 |
| GET | /api/v1/product/localized/{spuId}/{locale} | 查询指定语言文案 |

---

## 📋 数据库表

1. **pim_product_spu** - 商品 SPU 主表
2. **pim_product_sku** - 商品 SKU 表
3. **pim_product_localized** - 商品多语言文案表
4. **pim_product_listing** - 店铺商品映射表
5. **sys_distribute_task** - 分发任务表

---

## 🚀 启动方式

```bash
cd backend/erp-product
mvn spring-boot:run
```

---

## 📝 开发日志

- 2026-03-08: 完成基础 CRUD 功能开发
