package com.maxinhai.platform.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;

/**
 * @ClassName：JpaQueryUtils
 * @Author: XinHai.Ma
 * @Date: 2025/9/1 16:18
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Component
public class JpaQueryUtils {

    private final EntityManager entityManager;

    /**
     * 注入EntityManager
     * @param entityManager
     */
    public JpaQueryUtils(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * 执行原生SQL查询，返回实体列表
     *
     * @param sql         SQL语句
     * @param entityClass 实体类
     * @param params      参数
     * @param <T>         实体类型
     * @return 实体列表
     */
    public <T> List<T> queryNativeSql(String sql, Class<T> entityClass, Object... params) {
        Assert.hasText(sql, "SQL语句不能为空");
        Assert.notNull(entityClass, "实体类不能为空");

        Query query = entityManager.createNativeQuery(sql, entityClass);
        setParameters(query, params);
        return query.getResultList();
    }

    /**
     * 执行原生SQL查询，返回分页结果
     *
     * @param sql         SQL语句
     * @param countSql    统计总数的SQL语句
     * @param entityClass 实体类
     * @param pageable    分页参数
     * @param params      参数
     * @param <T>         实体类型
     * @return 分页结果
     */
    public <T> Page<T> queryNativeSqlWithPage(String sql, String countSql, Class<T> entityClass,
                                              Pageable pageable, Object... params) {
        Assert.hasText(sql, "SQL语句不能为空");
        Assert.hasText(countSql, "统计SQL语句不能为空");

        // 查询数据列表
        Query dataQuery = entityManager.createNativeQuery(sql, entityClass);
        setParameters(dataQuery, params);
        applyPageable(dataQuery, pageable);
        List<T> content = dataQuery.getResultList();

        // 查询总条数
        Query countQuery = entityManager.createNativeQuery(countSql);
        setParameters(countQuery, params);
        long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 执行JPQL查询，返回实体列表
     *
     * @param jpql        JPQL语句
     * @param entityClass 实体类
     * @param params      参数
     * @param <T>         实体类型
     * @return 实体列表
     */
    public <T> List<T> queryJpql(String jpql, Class<T> entityClass, Object... params) {
        Assert.hasText(jpql, "JPQL语句不能为空");
        Assert.notNull(entityClass, "实体类不能为空");

        TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
        setParameters(query, params);
        return query.getResultList();
    }

    /**
     * 执行PostgreSQL特定查询，支持JSONB字段操作
     *
     * @param sql         包含PostgreSQL特定语法的SQL
     * @param entityClass 实体类
     * @param params      参数
     * @param <T>         实体类型
     * @return 实体列表
     */
    public <T> List<T> queryPostgresSql(String sql, Class<T> entityClass, Object... params) {
        // PostgreSQL特定查询本质上也是原生SQL，这里单独封装便于区分和扩展
        return queryNativeSql(sql, entityClass, params);
    }

    /**
     * 执行查询返回Map结果（适用于多表关联查询）
     *
     * @param sql    SQL语句
     * @param params 参数
     * @return Map列表，key为字段名，value为值
     */
    public List<Map<String, Object>> queryForMap(String sql, Object... params) {
        Assert.hasText(sql, "SQL语句不能为空");

        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(org.hibernate.query.Query.class).setResultTransformer(
                org.hibernate.transform.AliasToEntityMapResultTransformer.INSTANCE
        );
        setParameters(query, params);
        return query.getResultList();
    }

    /**
     * 执行更新/删除的原生SQL
     *
     * @param sql    SQL语句
     * @param params 参数
     * @return 受影响的行数
     */
    public int executeUpdate(String sql, Object... params) {
        Assert.hasText(sql, "SQL语句不能为空");

        Query query = entityManager.createNativeQuery(sql);
        setParameters(query, params);
        return query.executeUpdate();
    }

    /**
     * 基于Criteria查询实体列表
     *
     * @param entityClass 实体类
     * @param <T>         实体类型
     * @return 实体列表
     */
    public <T> List<T> queryByCriteria(Class<T> entityClass) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        return entityManager.createQuery(cq).getResultList();
    }

    // ------------------- 新增的总条数查询方法 -------------------

    /**
     * 原生SQL查询总条数
     *
     * @param countSql 统计SQL语句，如"SELECT COUNT(*) FROM table WHERE condition"
     * @param params   参数
     * @return 总条数
     */
    public long countByNativeSql(String countSql, Object... params) {
        Assert.hasText(countSql, "统计SQL语句不能为空");

        Query query = entityManager.createNativeQuery(countSql);
        setParameters(query, params);

        Object result = query.getSingleResult();
        if (result instanceof Number) {
            return ((Number) result).longValue();
        }
        return 0;
    }

    /**
     * JPQL查询总条数
     *
     * @param countJpql 统计JPQL语句，如"SELECT COUNT(e) FROM Entity e WHERE e.status = :status"
     * @param params    参数
     * @return 总条数
     */
    public long countByJpql(String countJpql, Object... params) {
        Assert.hasText(countJpql, "统计JPQL语句不能为空");

        Query query = entityManager.createQuery(countJpql);
        setParameters(query, params);

        Object result = query.getSingleResult();
        if (result instanceof Number) {
            return ((Number) result).longValue();
        }
        return 0;
    }

    /**
     * 统计实体类对应的表的总记录数
     *
     * @param entityClass 实体类
     * @param <T>         实体类型
     * @return 总记录数
     */
    public <T> long countAll(Class<T> entityClass) {
        Assert.notNull(entityClass, "实体类不能为空");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(entityClass);
        cq.select(cb.count(root));

        return entityManager.createQuery(cq).getSingleResult();
    }

    /**
     * 为查询设置参数
     */
    private void setParameters(Query query, Object... params) {
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]); // JPA参数索引从1开始
            }
        }
    }

    /**
     * 应用分页参数
     */
    private void applyPageable(Query query, Pageable pageable) {
        if (pageable != null) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
    }
}
