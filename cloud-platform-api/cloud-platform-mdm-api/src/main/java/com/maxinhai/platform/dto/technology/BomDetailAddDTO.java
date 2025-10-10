package com.maxinhai.platform.dto.technology;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "BOM明细新增DTO")
public class BomDetailAddDTO {

    @ApiModelProperty(value = "物料清单ID")
    private String bomId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料数量")
    private BigDecimal qty;
    @ApiModelProperty(value = "父级ID")
    private String parentId;

}
