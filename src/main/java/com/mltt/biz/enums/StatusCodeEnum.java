package com.mltt.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusCodeEnum {
    SC200(200, "操作成功"),
    SC999(999, "操作失败"),
    SC401(401, "匿名用户访问权限资源时的异常"),
    SC403(403, "无访问权限，请联系管理员授予权限"),
    SC404(404, "请求的资源不存在"),
    SC500(500, "系统异常，请稍后重试");
    private final Integer code;
    private final String msg;
}
