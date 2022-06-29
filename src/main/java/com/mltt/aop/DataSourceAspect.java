package com.mltt.aop;

import com.mltt.Annotation.DynamicDataSource;
import com.mltt.bean.DataSourceNames;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 多数据源，切面处理类
 */
@Aspect
@Component
public class DataSourceAspect implements Ordered {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 针对上面注解做切面拦截
     */
    @Pointcut("@annotation(com.mltt.Annotation.DynamicDataSource)")
    public void dataSourcePointCut() {}

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        DynamicDataSource ds = method.getAnnotation(DynamicDataSource.class);
        if(ds == null){
            //如果没有注解,使用默认数据源
            com.mltt.bean.DynamicDataSource.setDataSource(DataSourceNames.MASTER);
        }else {
            //根据注解中设置的数据源名称,选择对应的数据源
            com.mltt.bean.DynamicDataSource.setDataSource(ds.name());
            logger.debug("set datasource is : " + ds.name());
            System.out.println("set datasource is : " + ds.name());
        }

        try {
            return point.proceed();
        } finally {
            //清除数据源配置
            com.mltt.bean.DynamicDataSource.clearDataSource();
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
