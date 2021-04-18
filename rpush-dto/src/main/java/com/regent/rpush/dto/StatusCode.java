package com.regent.rpush.dto;

public enum StatusCode {

    SUCCESS(0, "请求成功"),
    FAILURE(500, "请求失败"),
    UNAUTHORIZED(401, "用户没有访问权限，需要进行身份认证"),
    VALIDATE_FAIL(400, "参数校验失败"),
    ;

    private final int code;
    private final String msg;

    StatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
