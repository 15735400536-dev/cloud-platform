package com.maxinhai.platform.dto.technology;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("DTO")
public class BomDetailEditDTO {

    @ApiModelProperty("物料清单ID")
    private String bomId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料数量")
    private BigDecimal qty;
    @ApiModelProperty("父级ID")
    private String parentId;

}
