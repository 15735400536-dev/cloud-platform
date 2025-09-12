package com.maxinhai.platform.exception;

import lombok.Getter;

/**
 * @ClassName：FileException
 * @Author: XinHai.Ma
 * @Date: 2025/9/12 11:05
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Getter
public class FileStorageException extends RuntimeException {

    private final int code;

    public FileStorageException(String message) {
        super(message);
        this.code = 500;
    }

    public FileStorageException(String message, int code) {
        super(message);
        this.code = code;
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
    }

}
