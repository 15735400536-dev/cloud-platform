package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName：CustomSql
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 16:20
 * @Description: 自定义SQL
 */
@Data
@TableName("rep_sql")
public class CustomSql extends RecordEntity {

    /**
     * sql语句
     */
    private String sql;
    /**
     * 数据源ID
     */
    private String dataSourceId;

    /**
     * 构建条件查询语句
     *
     * @param conditionList 查询条件
     * @return
     */
    public String buildQuerySql(List<CustomCondition> conditionList) {
        return new StringBuffer("SELECT * FROM (")
                .append(sql)
                .append(") WHERE ")
                .append(conditionList.stream().map(condition -> condition.build()).collect(Collectors.joining()))
                .toString();
    }

}
