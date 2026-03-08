package com.erp.jp.common.exception;

/**
 * 禁止访问异常
 */
public class ForbiddenException extends BusinessException {
    
    public ForbiddenException() {
        super(403, "Forbidden");
    }
    
    public ForbiddenException(String message) {
        super(403, message);
    }
}
