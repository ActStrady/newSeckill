package org.seckill.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
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
    public void all() {}

    @Before(value = "all()")
    public void before(JoinPoint joinPoint) {
        joinPoint.getArgs();
        log.info("测试一下");
    }

    @AfterReturning(value = "all()")
    public void after() {
        log.info("测试两下");
    }
}
