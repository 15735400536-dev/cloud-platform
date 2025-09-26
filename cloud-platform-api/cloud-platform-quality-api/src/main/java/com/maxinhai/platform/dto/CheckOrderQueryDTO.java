package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.CheckOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "检测单分页查询DTO")
public class CheckOrderQueryDTO extends PageSearch<CheckOrderVO>{

    /**
     * 检测单编码
     */
    @ApiModelProperty(value = "检测单编码")
    private String orderCode;

}
