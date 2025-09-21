package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

public interface CommonCodeCheckService {

    /**
     * 校验编码唯一性
     *
     * @param entityClass 实体类class
     * @param codeField   编码字段名
     * @param codeValue   编码值
     * @param excludeId   排除的ID（用于更新时排除自身）
     * @return 是否唯一 true-唯一 false-已存在
     */
    <T> boolean isCodeUnique(Class<T> entityClass, String codeField, String codeValue, String excludeId);

    /**
     * 校验编码唯一性（推荐使用，编译期即可检查字段是否存在，减少运行时错误）
     *
     * @param entityClass 实体类class
     * @param codeField   编码字段名
     * @param codeValue   编码值
     * @param excludeId   排除的ID（用于更新时排除自身）
     * @return 是否唯一 true-唯一 false-已存在
     */
    <T, R> boolean isCodeUnique(Class<T> entityClass, SFunction<T, R> codeField, R codeValue, String excludeId);

    /**
     * 校验编码唯一性（新增场景）
     */
    default <T> boolean isCodeUnique(Class<T> entityClass, String codeField, String codeValue) {
        return isCodeUnique(entityClass, codeField, codeValue, null);
    }

    /**
     * 校验编码唯一性（新增场景）
     */
    default <T, R> boolean isCodeUnique(Class<T> entityClass, SFunction<T, R> codeField, R codeValue) {
        return isCodeUnique(entityClass, codeField, codeValue, null);
    }
}
