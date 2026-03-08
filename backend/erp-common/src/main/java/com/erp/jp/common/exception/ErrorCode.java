package com.erp.jp.common.exception;

/**
 * 错误码定义
 */
public class ErrorCode {
    
    // 通用错误
    public static final int SUCCESS = 200;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_ERROR = 500;
    
    // 业务错误码段 (1000-9999)
    
    // 商品模块 (1000-1999)
    public static final int PRODUCT_NOT_FOUND = 1001;
    public static final int SKU_NOT_FOUND = 1002;
    public static final int SPU_CODE_DUPLICATE = 1003;
    public static final int SKU_CODE_DUPLICATE = 1004;
    public static final int INVALID_JAN_CODE = 1005;
    
    // 订单模块 (2000-2999)
    public static final int ORDER_NOT_FOUND = 2001;
    public static final int ORDER_STATUS_INVALID = 2002;
    
    // 库存模块 (3000-3999)
    public static final int INSUFFICIENT_STOCK = 3001;
    public static final int WAREHOUSE_NOT_FOUND = 3002;
    
    // 店铺模块 (4000-4999)
    public static final int SHOP_NOT_FOUND = 4001;
    public static final int SHOP_AUTH_FAILED = 4002;
    
    // 用户模块 (5000-5999)
    public static final int USER_NOT_FOUND = 5001;
    public static final int USER_PASSWORD_ERROR = 5002;
    public static final int USER_LOCKED = 5003;
    
    // 系统模块 (6000-6999)
    public static final int TASK_NOT_FOUND = 6001;
    public static final int TASK_EXECUTION_FAILED = 6002;
}
