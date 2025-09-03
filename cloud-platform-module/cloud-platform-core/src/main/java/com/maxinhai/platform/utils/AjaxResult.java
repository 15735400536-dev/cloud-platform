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

    public static <T> AjaxResult<T> success(int code, String msg, T data) {
        return new AjaxResult(code, msg, data);
    }

    public static <T> AjaxResult<T> success(String msg, T data) {
        return new AjaxResult(HttpStatus.OK.value(), msg, data);
    }

    public static <T> AjaxResult<T> success(String msg) {
        return new AjaxResult(HttpStatus.OK.value(), msg, null);
    }

    public static <T> AjaxResult<T> success(T data) {
        return new AjaxResult(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    public static <T> AjaxResult<T> success() {
        return new AjaxResult(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), null);
    }

    public static <T> AjaxResult<T> fail(int code, String msg, T data) {
        return new AjaxResult(code, msg, data);
    }

    public static <T> AjaxResult<T> fail(String msg, T data) {
        return new AjaxResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, data);
    }

    public static <T> AjaxResult<T> fail(String msg) {
        return new AjaxResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, null);
    }

    public static <T> AjaxResult<T> fail(T data) {
        return new AjaxResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    public static <T> AjaxResult<T> fail() {
        return new AjaxResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.OK.getReasonPhrase(), null);
    }

}
