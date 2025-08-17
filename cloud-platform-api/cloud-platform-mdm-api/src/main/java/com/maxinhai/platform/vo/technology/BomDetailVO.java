package com.maxinhai.platform.vo.technology;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("VO")
public class BomDetailVO {

    @ApiModelProperty("物料清单ID")
    private String bomId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("物料数量")
    private BigDecimal qty;
    @ApiModelProperty("父级ID")
    private String parentId;

}
