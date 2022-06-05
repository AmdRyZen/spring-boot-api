package com.mltt.bean;


import brave.Tracer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 初始化 {@linkplain HandlerMethodReturnValueHandlerProxy} 注入参数 Tracer并代理 {@linkplain HandlerMethodReturnValueHandler}
 * 添加到 handlerAdapter.getReturnValueHandlers()中
 */
@Configuration
public class RestReturnValueHandlerConfig implements InitializingBean {
    @Resource
    private RequestMappingHandlerAdapter handlerAdapter;
    @Resource
    private Tracer trace;

    @Override
    public void afterPropertiesSet() {
        List<HandlerMethodReturnValueHandler> list = handlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> newList = new ArrayList<>();
        if (list != null) {
            for (HandlerMethodReturnValueHandler valueHandler : list) {
                if (valueHandler instanceof RequestResponseBodyMethodProcessor) {
                    HandlerMethodReturnValueHandlerProxy proxy = new HandlerMethodReturnValueHandlerProxy(valueHandler, trace);
                    newList.add(proxy);
                } else {
                    newList.add(valueHandler);
                }
            }
        }

        handlerAdapter.setReturnValueHandlers(newList);
    }
}