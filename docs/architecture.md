# 日本跨境电商 ERP - 架构设计文档

**版本**: v1.0  
**创建时间**: 2026-03-09

---

## 🏗️ 系统架构

### 分层架构

```
┌─────────────────────────────────────────────────────────────┐
│                      Presentation Layer                      │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  Vue 3 + TypeScript + Element Plus                    │  │
│  │  - 店铺管理页面                                         │  │
│  │  - 订单管理页面                                         │  │
│  │  - 商品管理页面                                         │  │
│  │  - 库存管理页面                                         │  │
│  │  - 数据看板                                            │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            ↓ REST API
┌─────────────────────────────────────────────────────────────┐
│                       Gateway Layer                          │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  erp-gateway                                          │  │
│  │  - 统一认证 (JWT)                                      │  │
│  │  - 权限校验                                            │  │
│  │  - 限流熔断                                            │  │
│  │  - 请求日志                                            │  │
│  │  - 统一异常处理                                         │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                      Business Layer                          │
│  ┌───────────┐ ┌───────────┐ ┌───────────┐ ┌───────────┐   │
│  │ StoreSvc  │ │ OrderSvc  │ │ ProductSvc│ │InventorySvc│  │
│  └───────────┘ └───────────┘ └───────────┘ └───────────┘   │
│  ┌───────────┐ ┌───────────┐ ┌───────────┐                 │
│  │ SyncSvc   │ │ JobSvc    │ │ ReportSvc │                 │
│  └───────────┘ └───────────┘ └───────────┘                 │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                   Integration Layer                          │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  Platform Adapter (SPI)                               │  │
│  │  ┌─────────────────┐ ┌─────────────────┐             │  │
│  │  │ RakutenAdapter  │ │ AmazonAdapter   │ (预留)      │  │
│  │  │ - OAuth         │ │ - SP-API        │             │  │
│  │  │ - Order API     │ │ - Order API     │             │  │
│  │  │ - Product API   │ │ - Product API   │             │  │
│  │  │ - Inventory API │ │ - Inventory API │             │  │
│  │  └─────────────────┘ └─────────────────┘             │  │
│  │  ┌─────────────────┐                                 │  │
│  │  │ MockAdapter     │ (开发/测试模式)                  │  │
│  │  └─────────────────┘                                 │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                      Persistence Layer                       │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  MyBatis-Plus + HikariCP                              │  │
│  │  - Mapper 接口                                        │  │
│  │  - 实体类 (Entity)                                     │  │
│  │  - DTO/VO                                             │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                       Data Layer                             │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐        │
│  │ MySQL 8.0    │ │ Redis        │ │ File Storage │        │
│  │ (主从复制)    │ │ (缓存/会话)   │ │ (日志/报表)   │        │
│  └──────────────┘ └──────────────┘ └──────────────┘        │
└─────────────────────────────────────────────────────────────┘
```

---

## 📦 模块设计

### 模块依赖关系

```
erp-japen (parent)
├── erp-common          # 公共模块 (无依赖)
├── erp-gateway         # 网关模块 (依赖 common)
├── erp-system          # 系统管理 (依赖 common)
├── erp-product         # 商品中心 (依赖 common)
├── erp-store           # 店铺管理 (依赖 common, system)
├── erp-order           # 订单中心 (依赖 common, product, store)
├── erp-inventory       # 库存管理 (依赖 common, product, store)
├── erp-platform-rakuten # 乐天集成 (依赖 common, store, order, inventory)
└── erp-job             # 任务调度 (依赖 common, 各业务模块)
```

### 各模块职责

#### erp-common
```
com.erp.jp.common
├── config/           # 配置类 (Cors, Jackson, MyBatis-Plus)
├── result/           # 统一响应 (R, ResultCode)
├── exception/        # 统一异常 (BusinessException, GlobalExceptionHandler)
├── util/             # 工具类 (DateUtils, JsonUtils, SignUtils)
├── annotation/       # 自定义注解
└── aspect/           # AOP 切面 (LogAspect)
```

#### erp-store
```
com.erp.jp.store
├── controller/       # REST API
│   ├── StoreController
│   ├── ShopGroupController
│   └── SyncConfigController
├── service/          # 业务逻辑
│   ├── StoreService
│   ├── ShopGroupService
│   └── SyncConfigService
├── mapper/           # 数据访问
│   ├── StoreMasterMapper
│   ├── ShopConfigMapper
│   └── ...
├── entity/           # 实体类
│   ├── StoreMaster
│   ├── ShopConfig
│   └── ...
└── dto/              # 数据传输对象
    ├── StoreVO
    ├── StoreCreateDTO
    └── ...
```

#### erp-platform-rakuten
```
com.erp.jp.platform.rakuten
├── config/           # 乐天 API 配置
│   └── RakutenProperties
├── client/           # API 客户端
│   ├── RakutenApiClient
│   ├── RakutenAuthClient
│   └── mock/         # 模拟实现
│       ├── MockOrderClient
│       ├── MockProductClient
│       └── MockInventoryClient
├── adapter/          # 适配器 (统一接口)
│   ├── RakutenOrderAdapter
│   ├── RakutenProductAdapter
│   └── RakutenInventoryAdapter
├── model/            # 乐天数据模型
│   ├── RakutenOrder
│   ├── RakutenProduct
│   └── ...
└── converter/        # 数据转换器
    ├── OrderConverter
    └── ProductConverter
```

#### erp-order
```
com.erp.jp.order
├── controller/
│   ├── OrderController
│   └── OrderShippingController
├── service/
│   ├── OrderService
│   ├── OrderSyncService
│   └── ShippingService
├── mapper/
│   ├── OrderMasterMapper
│   ├── OrderItemMapper
│   └── OrderShippingMapper
├── entity/
│   ├── OrderMaster
│   ├── OrderItem
│   └── OrderShipping
├── dto/
│   ├── OrderVO
│   ├── OrderCreateDTO
│   └── OrderShippingDTO
└── enums/
    ├── OrderStatus
    └── ShippingStatus
```

#### erp-inventory
```
com.erp.jp.inventory
├── controller/
│   ├── InventoryController
│   └── InventorySyncController
├── service/
│   ├── InventoryService
│   └── InventorySyncService
├── mapper/
│   ├── InventoryMapper
│   └── InventorySyncLogMapper
├── entity/
│   ├── Inventory
│   └── InventorySyncLog
└── dto/
    └── InventoryVO
```

#### erp-job
```
com.erp.jp.job
├── config/
│   └── SchedulerConfig
├── task/
│   ├── OrderSyncTask
│   ├── InventorySyncTask
│   └── ProductSyncTask
├── service/
│   └── JobLogService
├── entity/
│   └── JobExecutionLog
└── handler/
    └── TaskFailureHandler
```

---

## 🔌 平台适配器设计

### 统一接口

```java
// 订单适配器接口
public interface OrderAdapter {
    /**
     * 拉取订单
     * @param shopId 店铺 ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    List<PlatformOrder> fetchOrders(Long shopId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 获取订单详情
     */
    PlatformOrder getOrderDetail(Long shopId, String orderId);
    
    /**
     * 更新订单状态
     */
    void updateOrderStatus(Long shopId, String orderId, OrderStatus status);
}

// 库存适配器接口
public interface InventoryAdapter {
    /**
     * 同步库存到平台
     * @param shopId 店铺 ID
     * @param inventoryList 库存列表
     */
    void syncInventory(Long shopId, List<InventoryUpdate> inventoryList);
    
    /**
     * 获取平台库存
     */
    List<PlatformInventory> getInventory(Long shopId);
}

// 商品适配器接口
public interface ProductAdapter {
    /**
     * 拉取商品
     */
    List<PlatformProduct> fetchProducts(Long shopId, PageRequest page);
    
    /**
     * 同步商品到平台
     */
    void syncProduct(Long shopId, ProductInfo product);
}
```

### 乐天实现

```java
@Service
@Profile("!rakuten-mock")  // 非模拟模式
public class RakutenOrderAdapter implements OrderAdapter {
    
    private final RakutenApiClient apiClient;
    
    @Override
    public List<PlatformOrder> fetchOrders(Long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        // 调用真实乐天 API
        RakutenOrderResponse response = apiClient.getOrders(shopId, startTime, endTime);
        return convertToPlatformOrders(response);
    }
}

@Service
@Profile("rakuten-mock")  // 模拟模式
public class MockOrderAdapter implements OrderAdapter {
    
    @Override
    public List<PlatformOrder> fetchOrders(Long shopId, LocalDateTime startTime, LocalDateTime endTime) {
        // 返回模拟数据
        return generateMockOrders(shopId, startTime, endTime);
    }
}
```

---

## 📊 数据库设计

### 核心表

#### 订单主表 (t_order_master)
```sql
CREATE TABLE `t_order_master` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_no` VARCHAR(64) NOT NULL COMMENT '订单号',
  `tenant_id` BIGINT NOT NULL,
  `shop_id` BIGINT NOT NULL COMMENT '店铺 ID',
  `platform_code` VARCHAR(20) NOT NULL COMMENT '平台编码',
  `platform_order_id` VARCHAR(64) NOT NULL COMMENT '平台订单 ID',
  `order_status` TINYINT NOT NULL COMMENT '订单状态',
  `order_time` DATETIME NOT NULL COMMENT '下单时间',
  `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
  `ship_time` DATETIME DEFAULT NULL COMMENT '发货时间',
  `complete_time` DATETIME DEFAULT NULL COMMENT '完成时间',
  
  -- 买家信息
  `buyer_name` VARCHAR(100) DEFAULT NULL,
  `buyer_phone` VARCHAR(20) DEFAULT NULL,
  `buyer_email` VARCHAR(100) DEFAULT NULL,
  
  -- 收货地址
  `shipping_name` VARCHAR(100) DEFAULT NULL,
  `shipping_phone` VARCHAR(20) DEFAULT NULL,
  `shipping_province` VARCHAR(50) DEFAULT NULL,
  `shipping_city` VARCHAR(50) DEFAULT NULL,
  `shipping_district` VARCHAR(50) DEFAULT NULL,
  `shipping_address` VARCHAR(500) DEFAULT NULL,
  `shipping_zip` VARCHAR(10) DEFAULT NULL,
  
  -- 金额信息
  `total_amount` DECIMAL(10,2) NOT NULL COMMENT '订单总额',
  `pay_amount` DECIMAL(10,2) NOT NULL COMMENT '实付金额',
  `freight_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '运费',
  `discount_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '优惠金额',
  `currency` VARCHAR(10) DEFAULT 'JPY',
  
  -- 扩展信息
  `remark` VARCHAR(500) DEFAULT NULL,
  `platform_remark` VARCHAR(500) DEFAULT NULL COMMENT '平台备注',
  `raw_data` JSON DEFAULT NULL COMMENT '原始数据',
  
  -- 系统字段
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_platform_order` (`tenant_id`, `platform_code`, `platform_order_id`),
  KEY `idx_shop_time` (`shop_id`, `order_time`),
  KEY `idx_status` (`order_status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单主表';
```

#### 订单明细表 (t_order_item)
```sql
CREATE TABLE `t_order_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT NOT NULL,
  `order_no` VARCHAR(64) NOT NULL,
  `product_id` BIGINT DEFAULT NULL COMMENT '商品 ID',
  `sku_id` BIGINT DEFAULT NULL COMMENT 'SKU ID',
  `platform_product_id` VARCHAR(64) DEFAULT NULL COMMENT '平台商品 ID',
  `platform_sku_id` VARCHAR(64) DEFAULT NULL COMMENT '平台 SKU ID',
  `product_name` VARCHAR(200) NOT NULL,
  `product_image` VARCHAR(500) DEFAULT NULL,
  `sku_spec` VARCHAR(200) DEFAULT NULL COMMENT 'SKU 规格',
  `quantity` INT NOT NULL DEFAULT 1,
  `unit_price` DECIMAL(10,2) NOT NULL,
  `total_price` DECIMAL(10,2) NOT NULL,
  `currency` VARCHAR(10) DEFAULT 'JPY',
  PRIMARY KEY (`id`),
  KEY `idx_order` (`order_id`),
  KEY `idx_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单明细表';
```

#### 库存表 (t_inventory)
```sql
CREATE TABLE `t_inventory` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL,
  `warehouse_id` BIGINT NOT NULL COMMENT '仓库 ID',
  `product_id` BIGINT NOT NULL COMMENT '商品 ID',
  `sku_id` BIGINT NOT NULL COMMENT 'SKU ID',
  `stock_quantity` INT NOT NULL DEFAULT 0 COMMENT '库存数量',
  `available_quantity` INT NOT NULL DEFAULT 0 COMMENT '可用库存',
  `locked_quantity` INT NOT NULL DEFAULT 0 COMMENT '锁定库存',
  `warning_quantity` INT DEFAULT 10 COMMENT '预警数量',
  `currency` VARCHAR(10) DEFAULT 'JPY',
  `last_sync_time` DATETIME DEFAULT NULL COMMENT '最后同步时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_warehouse_sku` (`tenant_id`, `warehouse_id`, `sku_id`),
  KEY `idx_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存表';
```

#### 乐天店铺配置表 (t_rakuten_shop)
```sql
CREATE TABLE `t_rakuten_shop` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `store_id` BIGINT NOT NULL COMMENT '关联店铺 ID',
  `app_id` VARCHAR(100) NOT NULL COMMENT '乐天 App ID',
  `app_secret` VARCHAR(200) NOT NULL COMMENT 'App Secret(加密)',
  `access_token` TEXT DEFAULT NULL,
  `refresh_token` TEXT DEFAULT NULL,
  `token_expire_time` DATETIME DEFAULT NULL,
  `shop_name` VARCHAR(100) DEFAULT NULL COMMENT '乐天店铺名称',
  `shop_number` VARCHAR(50) DEFAULT NULL COMMENT '乐天店铺编号',
  `is_mock` TINYINT NOT NULL DEFAULT 0 COMMENT '是否模拟模式',
  `sync_config` JSON DEFAULT NULL COMMENT '同步配置',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_store_id` (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='乐天店铺配置表';
```

#### 同步日志表 (t_sync_log)
```sql
CREATE TABLE `t_sync_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT NOT NULL,
  `shop_id` BIGINT NOT NULL,
  `sync_type` VARCHAR(20) NOT NULL COMMENT '同步类型：ORDER/PRODUCT/INVENTORY',
  `sync_direction` VARCHAR(10) NOT NULL COMMENT '同步方向：FETCH/PUSH',
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME DEFAULT NULL,
  `status` TINYINT NOT NULL COMMENT '状态：0-进行中 1-成功 2-失败',
  `fetch_count` INT DEFAULT 0 COMMENT '拉取数量',
  `success_count` INT DEFAULT 0 COMMENT '成功数量',
  `fail_count` INT DEFAULT 0 COMMENT '失败数量',
  `error_message` TEXT DEFAULT NULL,
  `raw_response` JSON DEFAULT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_shop_type` (`shop_id`, `sync_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='同步日志表';
```

---

## 🔄 核心流程

### 订单同步流程

```
1. 定时任务触发 (OrderSyncTask)
        ↓
2. 获取需要同步的店铺列表
        ↓
3. 遍历店铺，调用 OrderAdapter.fetchOrders()
        ↓
4. 数据转换 (RakutenOrder → OrderMaster)
        ↓
5. 保存订单到数据库 (幂等处理)
        ↓
6. 更新商品库存 (订单商品 → 本地库存)
        ↓
7. 记录同步日志
        ↓
8. 失败重试队列
```

### 库存同步流程

```
1. 定时任务触发 (InventorySyncTask)
        ↓
2. 获取需要同步的店铺
        ↓
3. 查询本地库存 (按店铺关联仓库)
        ↓
4. 调用 InventoryAdapter.syncInventory()
        ↓
5. 记录同步结果
        ↓
6. 失败告警
```

---

## 🔒 安全设计

### 敏感数据加密

```java
@Component
public class EncryptionUtil {
    
    // API Secret 加密存储
    public String encrypt(String plaintext, String key) {
        // AES-256 加密
    }
    
    public String decrypt(String ciphertext, String key) {
        // AES-256 解密
    }
}
```

### 权限控制

- JWT Token 认证
- 基于角色的访问控制 (RBAC)
- 数据权限 (租户隔离)

---

## 📈 性能优化

### 数据库优化
- 合理索引
- 分表策略 (订单按时间分表)
- 读写分离预留

### 缓存策略
- 热点数据缓存 (店铺配置、商品信息)
- 接口响应缓存
- Redis 分布式锁

### 异步处理
- 订单同步异步化
- 邮件/通知异步发送
- 批量操作

---

## 📝 后续扩展

### 预留扩展点
1. 多平台支持 (亚马逊、Yahoo 等)
2. 多仓库支持
3. 多币种支持
4. 自定义报表
5. Webhook 通知

---

**文档维护**: 架构变更需同步更新此文档
