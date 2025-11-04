package com.maxinhai.platform.dto;

import com.maxinhai.platform.enums.CheckType;
import com.maxinhai.platform.vo.CheckLabelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName：CheckLabelQueryDTO
 * @Author: XinHai.Ma
 * @Date: 2025/8/19 16:25
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel(description = "标签分页查询DTO")
public class CheckLabelQueryDTO extends PageSearch<CheckLabelVO> {

    /**
     * 产品ID
     */
    @ApiModelProperty(value = "产品ID")
    private String productId;
    /**
     * 检测类型: 自建、互检、专检
     */
    @ApiModelProperty(value = "检测类型: 自建、互检、专检")
    private CheckType checkType = CheckType.ALL;

}
