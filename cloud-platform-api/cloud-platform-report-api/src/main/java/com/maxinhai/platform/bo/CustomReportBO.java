package com.maxinhai.platform.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName：CustomReportBO
 * @Author: XinHai.Ma
 * @Date: 2025/8/22 15:39
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel(description = "BO")
public class CustomReportBO {

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
    /**
     * 视图集合
     */
    private List<CustomViewBO> viewList;

}
