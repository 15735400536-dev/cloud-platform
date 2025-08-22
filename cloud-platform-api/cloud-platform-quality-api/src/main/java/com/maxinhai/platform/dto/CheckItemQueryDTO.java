package com.maxinhai.platform.dto;

import com.maxinhai.platform.enums.ControlType;
import com.maxinhai.platform.vo.CheckItemVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "DTO")
public class CheckItemQueryDTO extends PageSearch<CheckItemVO>{

    /**
     * 检测项编码
     */
    @ApiModelProperty(value = "检测项编码")
    private String itemCode;
    /**
     * 检测项名称
     */
    @ApiModelProperty(value = "检测项名称")
    private String itemName;
    /**
     * 检测类型: 定性、定量、手动输入
     */
    private ControlType controlType;
}
