package com.maxinhai.platform.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "入库明细DTO")
public class ReceiptDetailDTO {

    @ApiModelProperty(value = "入库单明细ID")
    private String receiptDetailId;
    @ApiModelProperty(value = "入库数量")
    private BigDecimal receiptQty;

}
