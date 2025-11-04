package com.maxinhai.platform.vo;

import com.maxinhai.platform.enums.CheckType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName：CheckLabelVO
 * @Author: XinHai.Ma
 * @Date: 2025/8/19 16:24
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel(description = "电子履历标签VO")
public class CheckLabelVO {

    /**
     * 标签编码
     */
    @ApiModelProperty(value = "标签编码")
    private String code;
    /**
     * 标签名称
     */
    @ApiModelProperty(value = "标签名称")
    private String name;
    /**
     * 产品ID
     */
    @ApiModelProperty(value = "产品ID")
    private String productId;
    @ApiModelProperty(value = "产品编码")
    private String productCode;
    @ApiModelProperty(value = "产品名称")
    private String productName;
    /**
     * 检测类型: 自建、互检、专检
     */
    @ApiModelProperty(value = "检测类型: 自建、互检、专检")
    private CheckType checkType = CheckType.UNKNOWN;
    /**
     * 检测项ID
     */
    @ApiModelProperty(value = "检测项ID")
    private String checkItemId;

}
