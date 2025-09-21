package com.maxinhai.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.maxinhai.platform.service.CommonCodeCheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 通用编码唯一性校验服务实现
 */
@Slf4j
@Service
public class CommonCodeCheckServiceImpl implements CommonCodeCheckService {

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public <T> boolean isCodeUnique(Class<T> entityClass, String codeField, String codeValue, String excludeId) {
        // 获取对应实体的Mapper
        BaseMapper<T> mapper = getBaseMapper(entityClass);

        // 构建查询条件
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(codeField, codeValue);

        // 如果是更新操作，排除自身记录
        if (excludeId != null) {
            queryWrapper.ne("id", excludeId);
        }

        // 执行查询
        Long count = mapper.selectCount(queryWrapper);

        // 结果为空说明编码唯一
        return count == 0;
    }

    @Override
    public <T, R> boolean isCodeUnique(Class<T> entityClass, SFunction<T, R> codeField, R codeValue, String excludeId) {

        // 获取实体对应的Mapper
        BaseMapper<T> mapper = getBaseMapper(entityClass);

        // 从Function中解析数据库字段名
        String dbFieldName = getDbFieldName(entityClass, codeField);

        // 构建查询条件
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(dbFieldName, codeValue);

        // 更新时排除自身
        if (excludeId != null) {
            // 假设主键字段名为"id"，如果不一致可改为动态获取
            queryWrapper.ne("id", excludeId);
        }

        // 执行查询
        Long count = mapper.selectCount(queryWrapper);

        // 无结果则表示唯一
        return count == 0L;
    }

    /**
     * 获取实体对应的BaseMapper
     */
    @SuppressWarnings("unchecked")
    private <T> BaseMapper<T> getBaseMapper(Class<T> entityClass) {
        String entitySimpleName = entityClass.getSimpleName();
        // 假设Mapper命名规则为实体名+Mapper，如User -> userMapper
        String mapperBeanName = Character.toLowerCase(entitySimpleName.charAt(0))
                + entitySimpleName.substring(1) + "Mapper";
        return (BaseMapper<T>) applicationContext.getBean(mapperBeanName);
    }

    /**
     * 根据 Lambda 表达式获取对应的数据库字段名
     *
     * @param func        实体类的 getter 方法 Lambda 表达式（如 User::getName）
     * @param entityClass 实体类的 Class 对象
     * @param <T>         实体类类型
     * @return 数据库字段名（如果解析失败返回 null）
     */
    private static <T> String getDbFieldName(Class<T> entityClass, SFunction<T, ?> func) {
        try {
            // 1. 解析 Lambda 表达式，获取元数据
            LambdaMeta lambdaMeta = LambdaUtils.extract(func);

            // 2. 从 Lambda 元数据中提取属性名（处理 getter 方法，如 getXXX -> XXX，isXXX -> XXX）
            String propertyName = getPropertyNameFromLambda(lambdaMeta);
            if (propertyName == null) {
                return null;
            }

            // 3. 获取实体类的属性-数据库字段映射关系
            Map<String, ColumnCache> columnMap = LambdaUtils.getColumnMap(entityClass);
            if (columnMap == null || columnMap.isEmpty()) {
                return null; // 实体类未被 MyBatis-Plus 扫描或无表结构信息
            }

            // 4. 根据属性名（大写）获取对应的数据库字段名（ColumnCache 的 key 是大写属性名）
            ColumnCache columnCache = columnMap.get(propertyName.toUpperCase());
            return columnCache != null ? columnCache.getColumn() : null;

        } catch (Exception e) {
            // 处理解析异常（如 Lambda 表达式无效、实体类配置错误等）
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从 Lambda 元数据中提取属性名（处理 getter 方法命名规范）
     */
    private static String getPropertyNameFromLambda(LambdaMeta lambdaMeta) {
        String methodName = lambdaMeta.getImplMethodName(); // 获取 Lambda 对应的方法名（如 getName、isEnabled）

        // 处理 getter 方法（get 开头，如 getName -> name）
        if (methodName.startsWith("get")) {
            if (methodName.length() <= 3) {
                return null; // 方法名不符合规范（如 getX 是合法的，但 get 本身不合法）
            }
            // 首字母小写（如 getName -> Name -> name）
            return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
        }

        // 处理布尔类型的 is 开头方法（如 isEnabled -> enabled）
        if (methodName.startsWith("is")) {
            if (methodName.length() <= 2) {
                return null; // 方法名不符合规范
            }
            // 首字母小写（如 isEnabled -> Enabled -> enabled）
            return methodName.substring(2, 3).toLowerCase() + methodName.substring(3);
        }

        return null; // 不是合法的 getter 方法
    }

}
