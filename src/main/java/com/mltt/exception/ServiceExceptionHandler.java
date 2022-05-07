package com.mltt.exception;

import com.mltt.utils.ApiResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class ServiceExceptionHandler extends Throwable {
    /**
     * 捕获其他异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ServiceException.class, IOException.class, Exception.class})
    public ApiResultUtils handle(ServiceException serviceException) {
        log.error("ServiceExceptionHandler code：{} message：{}", serviceException.getCode(), serviceException.getMessage());
        return ApiResultUtils.fail(serviceException.getCode(),serviceException.getMessage());
    }
}
