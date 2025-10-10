package com.maxinhai.platform.bo;

import com.maxinhai.platform.enums.ChartType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName：CustomViewBO
 * @Author: XinHai.Ma
 * @Date: 2025/8/22 15:14
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel(description = "BO")
public class CustomViewBO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 视图标识
     */
    private String key;
    /**
     * 视图标题
     */
    private String title;
    /**
     * 图表类型
     */
    private ChartType type;
    /**
     * 查询SQL
     */
    private String sqlId;
    /**
     * 自定义查询SQL
     */
    private CustomSqlBO customSql;

}
