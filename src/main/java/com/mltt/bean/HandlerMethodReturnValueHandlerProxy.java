package com.mltt.bean;

import brave.Tracer;
import com.mltt.utils.ApiResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 自定义rest统一结果返回值处理器
 */
public class HandlerMethodReturnValueHandlerProxy implements HandlerMethodReturnValueHandler {

    private HandlerMethodReturnValueHandler proxyObject;
    private Tracer trace;

    public HandlerMethodReturnValueHandlerProxy(HandlerMethodReturnValueHandler proxyObject, Tracer trace) {
        this.proxyObject = proxyObject;
        this.trace = trace;
    }
    /**
     * 不修改原始的支持类型
     */
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return proxyObject.supportsReturnType(returnType);
    }

    /**
     * 添加 traceId
     *
     * @param returnValue 返回值如果为{@linkplain ApiResultUtils}添加
     */
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {
        if (returnValue instanceof ApiResultUtils) {
            String traceIdString = trace.currentSpan().context().traceIdString();
            if (StringUtils.isNotEmpty(traceIdString)) {
                ((ApiResultUtils) returnValue).setTraceId(traceIdString);
            }
        }

        proxyObject.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }
}

