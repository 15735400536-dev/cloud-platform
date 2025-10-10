package com.maxinhai.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName：CustomReportVO
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 18:07
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel(description = "VO")
public class CustomReportVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 报表标识
     */
    private String key;
    /**
     * 报表标题
     */
    private String title;

}
