package com.erp.jp.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 日志切面
 */
@Slf4j
@Aspect
@Component
public class LogAspect {
    
    @Pointcut("execution(* com.erp.jp..controller..*(..))")
    public void controllerPointcut() {
    }
    
    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String methodName = point.getSignature().getName();
        long startTime = System.currentTimeMillis();
        
        log.info("====== 开始执行：{}.{}()", 
            point.getTarget().getClass().getSimpleName(), methodName);
        
        try {
            Object result = point.proceed();
            long endTime = System.currentTimeMillis();
            log.info("====== 执行完成：{}.{}(), 耗时：{}ms", 
                point.getTarget().getClass().getSimpleName(), methodName, (endTime - startTime));
            return result;
        } catch (Throwable e) {
            long endTime = System.currentTimeMillis();
            log.error("====== 执行异常：{}.{}(), 耗时：{}ms, 错误：{}", 
                point.getTarget().getClass().getSimpleName(), methodName, 
                (endTime - startTime), e.getMessage(), e);
            throw e;
        }
    }
}
