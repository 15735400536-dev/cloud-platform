package com.maxinhai.platform.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

@Slf4j
@Intercepts({
        // 拦截查询方法
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        ),
        // 拦截增删改方法
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
public class SlowSqlInterceptor implements Interceptor {

    // 慢查询阈值（默认500ms，可配置）
    private long slowThreshold = 500;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 1. 记录开始时间
        long startTime = System.currentTimeMillis();

        try {
            // 2. 执行原SQL（继续流程）
            return invocation.proceed();
        } finally {
            // 3. 计算耗时
            long costTime = System.currentTimeMillis() - startTime;

            // 4. 获取SQL和参数
            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
            Object parameter = invocation.getArgs()[1];
            String sql = mappedStatement.getBoundSql(parameter).getSql();// 带?的SQL

            // 5. 慢查询报警
            if (costTime > slowThreshold) {
                log.warn("[慢查询警告] 耗时: {}ms, SQL: {}, 参数: {}", costTime, sql, parameter);
            } else {
                log.info("[SQL监控] 耗时: {}ms, SQL: {}", costTime, sql);
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        // 生成代理对象（MyBatis工具方法，不用自己写）
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 从配置文件读阈值（比如application.yml里配）
        String threshold = properties.getProperty("slowThreshold");
        if (threshold != null) {
            this.slowThreshold = Long.parseLong(threshold);
        }
    }

}
