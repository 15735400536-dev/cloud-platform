package com.maxinhai.platform.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName：ReceiptDTO
 * @Author: XinHai.Ma
 * @Date: 2025/8/20 19:45
 * @Description: 入库DTO
 */
@Data
@ApiModel(description = "入库DTO")
public class ReceiptDTO {

    @ApiModelProperty(value = "入库单ID")
    private String receiptOrderId;
    @ApiModelProperty(value = "入库明细")
    private List<ReceiptDetailDTO> receiptDetailList;

}
