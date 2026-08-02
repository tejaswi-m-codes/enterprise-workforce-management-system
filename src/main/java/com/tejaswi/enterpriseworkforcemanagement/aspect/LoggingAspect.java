package com.tejaswi.enterpriseworkforcemanagement.aspect;


import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger=LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.example.service.*.*(..))")
    public void serviceMethods(){
    }

    @Before("serviceMethods()")
    public void beforeLog(JoinPoint joinPoint){
        
        String className=joinPoint.getTarget().getClass().getSimpleName();
        String methodName=joinPoint.getSignature().getName();

        Object[] arguments=joinPoint.getArgs();

        logger.info("Entering {}.{}",className,methodName);
        logger.info("Arguments : {}",Arrays.toString(arguments));

    }

    @After("serviceMethods()")
    public void logMethodExit(JoinPoint joinPoint){
        String className=joinPoint.getTarget().getClass().getSimpleName();
        String methodName=joinPoint.getSignature().getName();

        logger.info("Exiting {}.{}",className,methodName);
    }

    @AfterThrowing(pointcut = "serviceMethods()",throwing="exception")
    public void logException(JoinPoint joinPoint,Exception exception){

        String className=joinPoint.getTarget().getClass().getSimpleName();
        String methodName=joinPoint.getSignature().getName();

        logger.error("Exception in {}.{} : {}",className,methodName,exception.getMessage());

    } 
    
    @Around("serviceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable{

        long start=System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long end=System.currentTimeMillis();

        logger.info("{} executed in {} ms",joinPoint.getSignature().getName(),(end-start));

        return result;
    }




}
