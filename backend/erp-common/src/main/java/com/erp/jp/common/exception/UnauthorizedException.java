package com.erp.jp.common.exception;

/**
 * 未授权异常
 */
public class UnauthorizedException extends BusinessException {
    
    public UnauthorizedException() {
        super(401, "Unauthorized");
    }
    
    public UnauthorizedException(String message) {
        super(401, message);
    }
}
