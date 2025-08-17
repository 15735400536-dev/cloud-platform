package com.maxinhai.platform.exception;

/**
 * 自定义异常
 */
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

}
