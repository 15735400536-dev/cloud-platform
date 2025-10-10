package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @ClassName：CustomReportAddDTO
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 18:08
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel(description = "报表新增DTO")
public class CustomReportAddDTO {

    /**
     * 报表标识
     */
    private String key;
    /**
     * 报表标题
     */
    private String title;

}
