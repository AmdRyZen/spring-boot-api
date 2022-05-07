package com.mltt.exception;

import com.mltt.biz.enums.StatusCodeEnum;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    private Integer code;

    public ServiceException(StatusCodeEnum statusCodeEnum){
        super(statusCodeEnum.getMsg());
        this.code = statusCodeEnum.getCode();
    }

    /**
     * 自定义错误类型
     * @param code 自定义的错误码
     * @param msg 自定义的错误提示
     */
    public ServiceException(Integer code, String msg){
        super(msg);
        this.code = code;
    }
}
