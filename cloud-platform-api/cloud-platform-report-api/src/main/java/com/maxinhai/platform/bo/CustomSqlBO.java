package com.maxinhai.platform.bo;

import com.maxinhai.platform.po.CustomCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName：CustomSqlBO
 * @Author: XinHai.Ma
 * @Date: 2025/8/22 15:15
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel(description = "BO")
public class CustomSqlBO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * sql语句
     */
    private String sql;
    /**
     * 数据源ID
     */
    private String dataSourceId;
    /**
     * 数据源
     */
    private CustomDataSourceBO dataSource;
    /**
     * 自定义查询条件
     */
    private List<CustomConditionBO> conditionList;

    /**
     * 构建条件查询语句
     *
     * @param conditionList 查询条件
     * @return
     */
    public String buildQuerySql(List<CustomCondition> conditionList) {
        return new StringBuffer("SELECT * FROM (")
                .append(this.sql)
                .append(") WHERE ")
                .append(conditionList.stream().map(condition -> condition.build()).collect(Collectors.joining()))
                .toString();
    }

    /**
     * 构建条件查询语句
     *
     * @return
     */
    public String buildQuerySql() {
        return new StringBuffer("SELECT * FROM (")
                .append(this.sql)
                .append(") WHERE ")
                .append(this.conditionList.stream().map(condition -> condition.build()).collect(Collectors.joining()))
                .toString();
    }

}
