package com.erp.jp.common.exception;

/**
 * 资源未找到异常
 */
public class NotFoundException extends BusinessException {
    
    public NotFoundException(String resource) {
        super(404, resource + " not found");
    }
    
    public NotFoundException(Long id) {
        super(404, "Resource not found: " + id);
    }
}
