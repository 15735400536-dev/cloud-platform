package com.maxinhai.platform.dto.order;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.order.ReceiptOrderDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "出库单明细分页查询DTO")
public class ReceiptOrderDetailQueryDTO extends PageSearch<ReceiptOrderDetailVO> {

    /**
     * 入库单ID
     */
    @ApiModelProperty(value = "入库单ID")
    private String receiptOrderId;

}
