# ERP Japan - 基础框架

## 📦 公共模块 (erp-common)

### 统一返回结构

```java
R<T> response = R.ok(data);
R<T> response = R.fail("错误信息");
R<T> response = R.fail(500, "错误信息");
```

**返回格式：**
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

---

### 异常体系

#### 异常类

| 异常类 | HTTP 状态码 | 说明 |
|--------|-----------|------|
| `BusinessException` | 200 | 业务异常 |
| `ValidationException` | 400 | 参数验证异常 |
| `NotFoundException` | 404 | 资源未找到 |
| `UnauthorizedException` | 401 | 未授权 |
| `ForbiddenException` | 403 | 禁止访问 |

#### 使用示例

```java
// 抛出业务异常
throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "商品不存在");

// 抛出验证异常
throw new ValidationException("SPU 编码不能为空");

// 抛出未找到异常
throw new NotFoundException("商品", spuId);
```

#### 错误码定义

```java
// 通用错误码
ErrorCode.SUCCESS = 200
ErrorCode.BAD_REQUEST = 400
ErrorCode.UNAUTHORIZED = 401
ErrorCode.NOT_FOUND = 404
ErrorCode.INTERNAL_ERROR = 500

// 商品模块错误码
ErrorCode.PRODUCT_NOT_FOUND = 1001
ErrorCode.SKU_NOT_FOUND = 1002
ErrorCode.SPU_CODE_DUPLICATE = 1003
```

---

### 全局异常处理

所有异常自动被 `GlobalExceptionHandler` 捕获并转换为统一格式：

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException e) {
        return R.fail(e.getCode(), e.getMessage());
    }
}
```

---

### AOP 日志

自动记录 Controller 层方法执行日志：

```
====== 开始执行：ProductSpuController.create()
====== 执行完成：ProductSpuController.create(), 耗时：45ms
====== 执行异常：ProductSpuController.create(), 耗时：12ms, 错误：商品不存在
```

---

### 跨域配置

支持所有来源的跨域请求：

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    // 允许所有来源、方法、头部
}
```

---

### 工具类

#### TenantContext - 租户上下文

```java
// 设置租户 ID
TenantContext.setTenantId(1L);

// 获取租户 ID
Long tenantId = TenantContext.getTenantId();

// 清除
TenantContext.clear();
```

#### DateUtils - 日期工具

```java
// 格式化
String date = DateUtils.format(LocalDateTime.now());

// 解析
LocalDateTime dateTime = DateUtils.parse("2026-03-08 10:00:00");
```

#### JanCodeValidator - JAN 码校验

```java
// 校验格式
boolean valid = JanCodeValidator.isValid("4900001234567");

// 校验校验位
boolean checkDigit = JanCodeValidator.checkDigit("4900001234567");
```

---

### JSON 配置

自动配置 Jackson 支持 Java8 时间类型：

```java
@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        // 支持 LocalDateTime 等 Java8 时间类型
        // 时间格式化为字符串而非时间戳
    }
}
```

---

## 📋 使用指南

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.erp.jp</groupId>
    <artifactId>erp-common</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 使用统一返回

```java
@PostMapping
public R<ProductSpuResponse> create(@RequestBody ProductSpuCreateRequest request) {
    ProductSpu spu = service.createSpu(request);
    return R.ok(response);
}
```

### 3. 抛出异常

```java
public ProductSpu getById(Long spuId) {
    ProductSpu spu = getById(spuId);
    if (spu == null) {
        throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND, "商品不存在");
    }
    return spu;
}
```

### 4. 使用租户上下文

```java
// 在拦截器或切面中设置
public void preHandle(HttpServletRequest request) {
    String tenantId = request.getHeader("X-Tenant-ID");
    TenantContext.setTenantId(Long.valueOf(tenantId));
}

// 在 Service 中使用
public void createSpu(ProductSpu spu) {
    spu.setTenantId(TenantContext.getTenantId());
    save(spu);
}
```

---

## 🎯 最佳实践

### ✅ 推荐做法

1. **统一使用 R<T> 返回**
2. **使用业务异常而非直接返回错误**
3. **在 Service 层进行参数校验**
4. **使用租户上下文自动注入 tenantId**
5. **使用工具类进行日期/JAN 码处理**

### ❌ 避免做法

1. ~~直接返回 null 或错误信息~~
2. ~~捕获异常后不处理~~
3. ~~硬编码错误信息~~
4. ~~手动拼接 SQL 注入 tenantId~~

---

## 📊 组件清单

| 组件 | 位置 | 说明 |
|------|------|------|
| R<T> | result/ | 统一返回结构 |
| BusinessException | exception/ | 业务异常基类 |
| GlobalExceptionHandler | config/ | 全局异常处理 |
| LogAspect | aspect/ | 日志切面 |
| TenantContext | util/ | 租户上下文 |
| DateUtils | util/ | 日期工具 |
| JanCodeValidator | util/ | JAN 码校验 |
| CorsConfig | config/ | 跨域配置 |
| JacksonConfig | config/ | JSON 配置 |

---

## 🚀 下一步

- [ ] 添加权限拦截器
- [ ] 添加操作日志记录
- [ ] 添加 Redis 缓存工具
- [ ] 添加消息队列工具

---

*最后更新：2026-03-08*
