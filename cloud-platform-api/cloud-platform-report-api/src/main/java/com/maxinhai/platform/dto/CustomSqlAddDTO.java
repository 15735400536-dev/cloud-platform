package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @ClassName：CustomSqlAddDTO
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 18:07
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel(description = "查询SQL新增DTO")
public class CustomSqlAddDTO {

    /**
     * sql语句
     */
    private String sql;
    /**
     * 数据源ID
     */
    private String dataSourceId;

}
