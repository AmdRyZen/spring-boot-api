package com.mltt.Annotation;

import java.lang.annotation.*;

/**
 * 多数据源注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicDataSource {
    String name() default "";
}