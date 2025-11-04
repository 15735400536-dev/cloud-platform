package com.maxinhai.platform.handler;

import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.exception.CustomException;
import com.maxinhai.platform.utils.AjaxResult;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

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
