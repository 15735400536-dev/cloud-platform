package com.maxinhai.platform.utils;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * 统一返回结果
 * @param <T>
 */
@Data
public class AjaxResult<T> {

    private int code;
    private String msg;
    private T data;

    private AjaxResult() {
    }

    private AjaxResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> AjaxResult success(int code, String msg, T data) {
        return new AjaxResult(code, msg, data);
    }

    public static <T> AjaxResult success(String msg, T data) {
        return new AjaxResult(HttpStatus.OK.value(), msg, data);
    }

    public static <T> AjaxResult success(String msg) {
        return new AjaxResult(HttpStatus.OK.value(), msg, null);
    }

    public static <T> AjaxResult success(T data) {
        return new AjaxResult(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    public static <T> AjaxResult success() {
        return new AjaxResult(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), null);
    }

    public static <T> AjaxResult fail(int code, String msg, T data) {
        return new AjaxResult(code, msg, data);
    }

    public static <T> AjaxResult fail(String msg, T data) {
        return new AjaxResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, data);
    }

    public static <T> AjaxResult fail(String msg) {
        return new AjaxResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, null);
    }

    public static <T> AjaxResult fail(T data) {
        return new AjaxResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    public static <T> AjaxResult fail() {
        return new AjaxResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.OK.getReasonPhrase(), null);
    }

}
