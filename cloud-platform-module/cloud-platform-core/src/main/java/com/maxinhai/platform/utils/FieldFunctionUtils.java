package com.maxinhai.platform.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Java 11 通用字段获取工具类（基于Function）
 */
public class FieldFunctionUtils {

    /**
     * 批量获取单个对象的多个字段名+字段值（返回List，格式：字段名=值）
     * @param obj 目标实体对象
     * @param fieldFunctions 字段提取器列表
     * @param <T> 实体类型
     * @return 字段名+值的列表
     */
    public static <T> List<String> getFieldNamesAndValues(T obj, List<FieldFunction<T, ?>> fieldFunctions) {
        if (obj == null || fieldFunctions == null || fieldFunctions.isEmpty()) {
            return new ArrayList<>();
        }
        return fieldFunctions.stream()
                .map(f -> f.getFieldName() + "=" + f.getFieldValue(obj))
                .collect(Collectors.toList());
    }

    /**
     * 批量获取单个对象的多个字段名+字段值（返回Map，键：字段名，值：字段值）
     * @param obj 目标实体对象
     * @param fieldFunctions 字段提取器列表
     * @param <T> 实体类型
     * @return 字段名-值的Map（Java 11支持Map.ofNullable等特性）
     */
    public static <T> Map<String, Object> getFieldNameValueMap(T obj, List<FieldFunction<T, ?>> fieldFunctions) {
        if (obj == null || fieldFunctions == null || fieldFunctions.isEmpty()) {
            return Map.of(); // Java 9+（兼容Java11）的空Map，不可变
        }
        return fieldFunctions.stream()
                .collect(Collectors.toMap(
                        FieldFunction::getFieldName,
                        f -> f.getFieldValue(obj),
                        (oldVal, newVal) -> newVal, // 字段名重复时取新值（避免冲突）
                        java.util.LinkedHashMap::new // 保持字段顺序
                ));
    }

    /**
     * 复用同一个FieldFunction，获取多个对象的指定字段值
     * @param objList 实体对象列表
     * @param fieldFunction 单个字段提取器
     * @param <T> 实体类型
     * @param <R> 字段值类型
     * @return 每个对象的该字段值列表
     */
    public static <T, R> List<R> getFieldValues(List<T> objList, FieldFunction<T, R> fieldFunction) {
        if (objList == null || objList.isEmpty() || fieldFunction == null) {
            return new ArrayList<>();
        }
        return objList.stream()
                .map(obj -> fieldFunction.getFieldValue(obj))
                .collect(Collectors.toList());
    }

}
