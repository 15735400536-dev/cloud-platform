package com.maxinhai.platform.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "出库明细DTO")
public class IssueDetailDTO {

    @ApiModelProperty(value = "出库单明细ID")
    private String issueDetailId;
    @ApiModelProperty(value = "出库数量")
    private BigDecimal issueQty;

}
