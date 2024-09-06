package com.krnal.products.scoutinghub.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

import static com.krnal.products.scoutinghub.utils.Utilities.createLogMessage;

@Aspect
@Configuration
public class ApplicationLogAspect {
    Logger logger = LoggerFactory.getLogger(ApplicationLogAspect.class);

    @Around("execution(* com.krnal.products.scoutinghub.*..*(..))")
    public Object aroundLogic(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getTarget().getClass() + "." + joinPoint.getSignature().getName();
        long before = System.currentTimeMillis();
        logger.debug(createLogMessage(method, "call", "Start", "args", Arrays.toString(joinPoint.getArgs())));
        Object proceed = joinPoint.proceed();
        logger.info(createLogMessage(method, "call", "SUCCESS", "execTime", (System.currentTimeMillis() - before) + "ms"));
        return proceed;
    }

}
