package com.maxinhai.platform.utils;

import java.util.Optional;
import java.util.function.Function;

/**
 * 字段名 + 字段值提取器（Function封装）
 *
 * @param <T> 实体类型（如User）
 * @param <R> 字段值类型（如String、Integer）
 */
public class FieldFunction<T, R> {

    // 字段名（如"username"，与实体字段对应）
    private final String fieldName;
    // 字段值提取器：传入实体对象T，返回字段值R
    private final Function<T, R> fieldExtractor;

    // 构造方法（私有化，通过静态方法创建，更规范）
    private FieldFunction(String fieldName, Function<T, R> fieldExtractor) {
        this.fieldName = fieldName;
        this.fieldExtractor = fieldExtractor;
    }

    // 静态工厂方法：创建FieldFunction（Java 11支持var，简化调用）
    public static <T, R> FieldFunction<T, R> of(String fieldName, Function<T, R> fieldExtractor) {
        // 校验入参，避免空值
        if (fieldName == null || fieldName.isBlank()) {
            throw new IllegalArgumentException("字段名不能为空！");
        }
        if (fieldExtractor == null) {
            throw new IllegalArgumentException("字段提取器Function不能为空！");
        }
        return new FieldFunction<>(fieldName, fieldExtractor);
    }

    // 获取字段名
    public String getFieldName() {
        return fieldName;
    }

    // 提取字段值（支持空值处理，Java 11的Optional优化）
    public R getFieldValue(T obj) {
        if (obj == null) {
            return null;
        }
        // 空值兜底：根据业务需求调整（比如返回默认值）
        return Optional.ofNullable(fieldExtractor.apply(obj)).orElse(null);
    }

    // 重载：提取字段值并指定默认值
    public R getFieldValue(T obj, R defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        return Optional.ofNullable(fieldExtractor.apply(obj)).orElse(defaultValue);
    }

}
