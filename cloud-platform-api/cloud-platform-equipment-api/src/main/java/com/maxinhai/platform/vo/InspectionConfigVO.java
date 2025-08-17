package com.maxinhai.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("VO")
public class InspectionConfigVO {

    @ApiModelProperty("主键ID")
    private String id;

}
