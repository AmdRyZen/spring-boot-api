package com.mltt.utils;

import com.mltt.biz.enums.StatusCodeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApiResultUtils<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;
    private String traceId;

    public static <T> ApiResultUtils<T> success(T data) {
        return ApiResultUtils.success(StatusCodeEnum.SC200.getMsg(), data);
    }

    public static <T> ApiResultUtils<T> success(String msg, T data) {
        ApiResultUtils<T> apiResult = new ApiResultUtils<>();
        apiResult.setCode(StatusCodeEnum.SC200.getCode());
        apiResult.setMsg(msg);
        apiResult.setData(data);
        return apiResult;
    }

    public static <T> ApiResultUtils<T> fail(Integer code, String msg) {
        ApiResultUtils<T> apiResult = new ApiResultUtils<>();
        apiResult.setCode(code);
        apiResult.setMsg(msg);
        return apiResult;
    }
}
