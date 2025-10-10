package com.maxinhai.platform.vo;

import com.maxinhai.platform.bo.CustomViewBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName：ReportPreviewVO
 * @Author: XinHai.Ma
 * @Date: 2025/8/22 15:12
 * @Description: 报表预览
 */
@Data
@ApiModel(description = "报表预览VO")
public class CustomReportPreviewVO {

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
