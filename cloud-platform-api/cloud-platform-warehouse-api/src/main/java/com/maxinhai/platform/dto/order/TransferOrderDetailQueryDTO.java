package com.maxinhai.platform.dto.order;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.order.TransferOrderDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("DTO")
public class TransferOrderDetailQueryDTO extends PageSearch<TransferOrderDetailVO> {

    /**
     * 移库单ID
     */
    @ApiModelProperty("移库单ID")
    private String transferOrderId;

}
