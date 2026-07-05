package com.claro.amx.cufjava.change_domestic_use_api.config;

import com.claro.amx.cufjava.change_domestic_use_api.util.LoggerUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggerAspect {

    @Around("execution(* com.claro.amx.cufjava.change_domestic_use_api.controller.impl.*.*(..))")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        var methodName = joinPoint.getSignature().getName();
        var className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        LoggerUtil.logEntry(log, className, methodName, joinPoint.getArgs());
        try {
            var result = joinPoint.proceed();
            LoggerUtil.logExit(log, className, methodName);
            return result;
        } catch (Exception e) {
            LoggerUtil.logError(log, className, methodName, e);
            throw e;
        }
    }

    @Around("execution(* com.claro.amx.cufjava.change_domestic_use_api.service.impl.*.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        var methodName = joinPoint.getSignature().getName();
        var className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        LoggerUtil.logEntry(log, className, methodName, joinPoint.getArgs());
        try {
            var result = joinPoint.proceed();
            LoggerUtil.logExit(log, className, methodName);
            return result;
        } catch (Exception e) {
            LoggerUtil.logError(log, className, methodName, e);
            throw e;
        }
    }
}
