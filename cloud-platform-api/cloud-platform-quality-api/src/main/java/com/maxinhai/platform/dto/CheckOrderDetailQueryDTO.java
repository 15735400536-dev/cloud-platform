package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.CheckOrderDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "DTO")
public class CheckOrderDetailQueryDTO extends PageSearch<CheckOrderDetailVO> {

    @ApiModelProperty(value = "检测单ID")
    private String checkOrderId;

}
