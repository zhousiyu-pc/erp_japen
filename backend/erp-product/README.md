# ERP Japan - Product Module

## 📦 商品中心（PIM）模块

### 开发进度

#### ✅ 已完成 (100%)
- [x] 数据库表结构设计（5 张表）
- [x] Entity 实体类（5 个）
  - ProductSpu
  - ProductSku
  - ProductLocalized
  - ProductListing
  - DistributeTask
- [x] Mapper 数据访问层（5 个）
- [x] Service 业务逻辑层（5 个）
- [x] Controller API 接口（4 个）
- [x] DTO 请求/响应对象（6 个）
- [x] 单元测试（服务层 + 控制器层）

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
| PUT | /api/v1/product/sku/{skuId}/status | 更新 SKU 状态 |
| DELETE | /api/v1/product/sku/{skuId} | 删除 SKU |

### 多语言文案
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/v1/product/localized | 保存/更新多语言文案 |
| GET | /api/v1/product/localized/{spuId}/{locale} | 查询指定语言文案 |
| GET | /api/v1/product/localized/{spuId} | 查询 SPU 所有语言文案 |

### 店铺映射
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/v1/product/listing | 创建店铺商品映射 |
| GET | /api/v1/product/listing/shop/{shopId} | 查询店铺商品列表 |
| GET | /api/v1/product/listing/spu/{spuId} | 查询 SPU 所有店铺映射 |
| PUT | /api/v1/product/listing/{listingId}/status | 更新同步状态 |

### 分发任务
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/v1/product/listing/distribute | 创建分发任务 |
| GET | /api/v1/product/listing/task/{taskId} | 查询任务进度 |
| PUT | /api/v1/product/listing/task/{taskId}/progress | 更新任务进度 |

---

## 📋 数据库表

1. **pim_product_spu** - 商品 SPU 主表
2. **pim_product_sku** - 商品 SKU 表（支持 JAN 码）
3. **pim_product_localized** - 商品多语言文案表
4. **pim_product_listing** - 店铺商品映射表
5. **sys_distribute_task** - 分发任务表

---

## 🧪 测试覆盖

### 服务层测试
- ✅ ProductSpuService - SPU 创建、状态更新
- ✅ ProductSkuService - SKU 创建、批量创建、查询
- ✅ ProductLocalizedService - 多语言保存、查询

### 控制器层测试
- ✅ ProductSpuController - API 创建、查询、验证
- ✅ ProductSkuController - SKU 管理 API
- ✅ ProductLocalizedController - 多语言 API
- ✅ ProductListingController - 店铺映射 API

---

## 🚀 启动方式

```bash
cd backend/erp-product
mvn clean test          # 运行测试
mvn spring-boot:run     # 启动服务
```

---

## 📝 功能特性

### ✅ 核心功能
1. **SPU/SKU 架构** - 支持多规格商品管理
2. **多语言支持** - 日文/中文/英文文案维护
3. **JAN 码支持** - 日本商品编码集成
4. **店铺映射** - 多平台 Listing 管理
5. **分发任务** - 批量上架到多平台
6. **状态管理** - 草稿/在售/停售状态流转

### ✅ 技术特性
1. **多租户隔离** - tenant_id 自动注入
2. **逻辑删除** - @TableLogic 注解
3. **自动填充** - 创建/更新时间自动填充
4. **参数校验** - @Validated 注解
5. **事务支持** - @Transactional 注解

---

## 📊 代码统计

| 类型 | 数量 | 说明 |
|------|------|------|
| Entity | 5 | 实体类 |
| Mapper | 5 | 数据访问层 |
| Service | 5 | 业务逻辑层 |
| Controller | 4 | API 接口层 |
| DTO | 6 | 数据传输对象 |
| Test | 3 | 单元测试类 |
| **总计** | **28** | **~1500 行代码** |

---

## 📅 开发日志

- 2026-03-08: 完成所有功能开发和单元测试 ✅
- 2026-03-08: 完成商品中心基础 CRUD 功能

---

## 🔜 后续优化

- [ ] 集成测试（Mock 外部依赖）
- [ ] API 文档（Swagger/OpenAPI）
- [ ] 性能优化（缓存、索引）
- [ ] 日志完善
- [ ] 异常处理统一化
