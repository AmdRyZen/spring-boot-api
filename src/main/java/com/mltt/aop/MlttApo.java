package com.mltt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MlttApo {
    //切入点：待增强的方法
    @Pointcut("execution(public * com.mltt.controller.*.*(..))")

    //切入点签名
    public void logAround() {
        System.out.println("pointCut签名。。。");
    }

    //环绕通知,环绕增强，相当于MethodInterceptor
    @Around("logAround()")
    public Object aroundAdvice(ProceedingJoinPoint pjp) {
        Object rtValue = null;
        try {
            Object[] args = pjp.getArgs();//得到方法执行所需的参数

            System.out.println("通知类中的aroundAdvice方法执行了。。前置");

            rtValue = pjp.proceed(args);//明确调用切入点方法（切入点方法）

            System.out.println("通知类中的aroundAdvice方法执行了。。返回");
            System.out.println("返回通知："+rtValue);

            return rtValue;
        } catch (Throwable e) {
            System.out.println("通知类中的aroundAdvice方法执行了。。异常");
            throw new RuntimeException(e);
        } finally {
            System.out.println("通知类中的aroundAdvice方法执行了。。后置");
        }
    }
}
