package com.maxinhai.platform;

import com.maxinhai.platform.po.CustomDataSource;
import com.maxinhai.platform.utils.JdbcUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SqlExecutor {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(CustomDataSource dataSource) {
        jdbcTemplate.setDataSource(null);
    }

    public List<T> selectList(String sql, Class<T> resultType) {
        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(resultType)
        );
    }

    public static void main(String[] args) throws Exception {
        JdbcUtils.setDatasource("jdbc:postgresql://localhost:5432/maxinhai", "org.postgresql.Driver", "postgres", "MaXinHai!970923");
        List<Map<String, Object>> records = JdbcUtils.selectList("select id, account, nickname from sys_user limit 100");
        records.stream().forEach(System.out::println);
    }

}
