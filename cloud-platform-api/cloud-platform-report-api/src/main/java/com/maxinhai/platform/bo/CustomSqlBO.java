package com.maxinhai.platform.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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

}
