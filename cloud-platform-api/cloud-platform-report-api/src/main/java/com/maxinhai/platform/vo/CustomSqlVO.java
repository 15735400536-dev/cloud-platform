package com.maxinhai.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName：CustomSqlVO
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 18:07
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel(description = "VO")
public class CustomSqlVO {

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

}
