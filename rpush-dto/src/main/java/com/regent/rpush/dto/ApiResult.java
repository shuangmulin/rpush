package com.regent.rpush.dto;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResult<T> implements Serializable {
    private static final long serialVersionUID = 7944370208725863484L;

    protected int code = StatusCode.SUCCESS.getCode();
    protected String msg = StatusCode.SUCCESS.getMsg();
    /**
     * 请求编号（幂等）
     */
    private String requestNo;
    private T data;

    public static <T> ApiResult<T> success() {
        return of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMsg(), null);
    }

    public static <T> ApiResult<T> unauthorized() {
        return of(StatusCode.SUCCESS.getCode(), StatusCode.UNAUTHORIZED.getMsg(), null);
    }

    public static <T> ApiResult<T> of(T data) {
        return of(0, StatusCode.SUCCESS.getMsg(), data);
    }

    public static <T> ApiResult<T> of(StatusCode statusCode, String message) {
        return of(statusCode.getCode(), message, null);
    }

    public static <T> ApiResult<T> of(StatusCode statusCode, T data) {
        return of(statusCode.getCode(), statusCode.getMsg(), data);
    }

    public static <T> ApiResult<T> of(int code, String message, T data) {
        return ApiResult.<T>builder()
                .code(code)
                .msg(message)
                .data(data)
                .build();
    }
}

