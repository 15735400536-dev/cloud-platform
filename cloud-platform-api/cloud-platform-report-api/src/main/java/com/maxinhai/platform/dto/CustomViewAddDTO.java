package com.maxinhai.platform.dto;

import com.maxinhai.platform.enums.ChartType;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @ClassName：CustomViewAddDTO
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 18:09
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel("DTO")
public class CustomViewAddDTO {

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

}
