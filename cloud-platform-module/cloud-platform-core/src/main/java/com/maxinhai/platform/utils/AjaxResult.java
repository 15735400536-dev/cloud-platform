package com.maxinhai.platform.utils;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * 统一返回结果
 *
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

    public static <T> AjaxResult<T> build(boolean flag, String msg, T data) {
        return new AjaxResult(flag ? HttpStatus.OK.value() : HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, data);
    }

    public static <T> AjaxResult<T> build(boolean flag, String msg) {
        return new AjaxResult(flag ? HttpStatus.OK.value() : HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, null);
    }

    /**
     * 重写toString方法，过滤值为null的字段
     */
    @Override
    public String toString() {
        List<String> fieldList = new ArrayList<>();

        // 只添加非null的字段
        fieldList.add("code=" + code);  // code为int，不可能为null

        if (msg != null) {
            fieldList.add("msg=" + msg);
        }

        if (data != null) {
            fieldList.add("data=" + data);
        }

        // 拼接成 "AjaxResult(字段1, 字段2, ...)" 格式
        return "AjaxResult(" + String.join(", ", fieldList) + ")";
    }
}
