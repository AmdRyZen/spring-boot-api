package com.mltt.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//表示次注解可以标注在类和方法上
@Target({ElementType.METHOD, ElementType.TYPE})
//运行时生效
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckLoginAnnotation {
    //定义一个变量，可以接受参数
    String desc() default " ";
}
