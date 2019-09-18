package org.seckill.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.seckill.exception.SeckillException;
import org.springframework.stereotype.Component;

/**
 * @author : ActStrady@tom.com
 * @date : 2019/9/16 15:44
 * @fileName : loggerAspect.java
 * @gitHub : https://github.com/ActStrady/seckill
 */
@Aspect
@Component
@Slf4j
public class loggerAspect {
    @Pointcut("execution(* org.seckill.service.*.*(..))")
    public void service() {
    }

    @Before("service()")
    public void before(JoinPoint jp) {
        String className = jp.getTarget().getClass().getName();
        String methodName = jp.getSignature().getName();
        log.info(className + "." + methodName + " - Successful Start - args={}", jp.getArgs());
    }

    @AfterReturning(value = "service()", returning = "retVal")
    public void after(JoinPoint jp, Object retVal) {
        String className = jp.getTarget().getClass().getName();
        String methodName = jp.getSignature().getName();
        log.info(className + "." + methodName + " - Successful End - return={}", retVal);
    }

    @AfterThrowing(pointcut = "service()", throwing = "ex")
    public void error(JoinPoint jp, SeckillException ex) {
        String className = jp.getTarget().getClass().getName();
        String methodName = jp.getSignature().getName();
        log.error(className + "." + methodName + " - " + ex.getMessage(), ex);
    }
}
