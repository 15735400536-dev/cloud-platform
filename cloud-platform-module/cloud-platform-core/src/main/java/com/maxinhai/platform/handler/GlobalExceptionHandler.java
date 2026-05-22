package com.maxinhai.platform.handler;

import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.exception.CustomException;
import com.maxinhai.platform.utils.AjaxResult;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.servlet.multipart.max-file-size:100MB}")
    private String maxFileSize;

    /**
     * 处理文件上传大小超限异常
     * <p>说明：MaxUploadSizeExceededException 默认由 Tomcat 容器抛出，发生在 SpringMVC 进入 Controller 之前，
     * 普通的 @ExceptionHandler 无法捕获。必须在配置文件中开启 spring.servlet.multipart.resolve-lazily=true，
     * 延迟文件解析，让异常抛到 Spring 异常处理器中才能捕获。</p>
     * <p>推荐配置：</p>
     * <pre>
     * spring:
     *   servlet:
     *     multipart:
     *       max-file-size: 100MB
     *       max-request-size: 100MB
     *       resolve-lazily: true
     * </pre>
     *
     * @param e 文件大小超限异常
     * @return 统一错误响应
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public AjaxResult handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        e.printStackTrace();
        return AjaxResult.fail("上传失败，文件大小超出系统限制！最大允许上传：" + maxFileSize);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public AjaxResult handleExpiredJwt(ExpiredJwtException e) {
        e.printStackTrace();
        return AjaxResult.fail(HttpStatus.UNAUTHORIZED.value(), "JWT已过期，请刷新令牌", e.getMessage());
    }

    // 处理 @RequestBody 参数校验失败
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        // 获取字段错误信息
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    // 处理 @RequestParam 参数校验失败
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleConstraintViolation(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    // 捕获业务异常（自定义异常）
    @ExceptionHandler(BusinessException.class)
    public AjaxResult<Void> handleBusinessException(BusinessException e) {
        e.printStackTrace();
        return AjaxResult.fail(e.getMessage());
    }

    // 捕获业务异常（自定义异常）
    @ExceptionHandler(CustomException.class)
    public AjaxResult<Void> handleCustomException(CustomException e) {
        e.printStackTrace();
        return AjaxResult.fail(e.getMessage(), null);
    }

    // 捕获其他所有异常（避免泄露敏感信息）
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResult<Void> handleException(Exception e) {
        e.printStackTrace();
        return AjaxResult.fail(e.getMessage(), null);
    }

}
