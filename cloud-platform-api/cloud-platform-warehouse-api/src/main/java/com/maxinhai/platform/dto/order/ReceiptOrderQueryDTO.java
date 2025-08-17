package com.maxinhai.platform.dto.order;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.order.ReceiptOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("DTO")
public class ReceiptOrderQueryDTO extends PageSearch<ReceiptOrderVO> {

    /**
     * 出库单号
     */
    @ApiModelProperty("出库单号")
    private String orderNo;
    /**
     * 仓库ID
     */
    @ApiModelProperty("仓库ID")
    private String warehouseId;

}
