package com.maxinhai.platform.dto.order;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.order.TransferOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("DTO")
public class TransferOrderQueryDTO extends PageSearch<TransferOrderVO> {

    /**
     * 移库单号
     */
    @ApiModelProperty("移库单号")
    private String transferNo;
    /**
     * 源仓库ID
     */
    @ApiModelProperty("源仓库ID")
    private String sourceWarehouseId;
    /**
     * 源仓库ID
     */
    @ApiModelProperty("源仓库ID")
    private String targetWarehouseId;

}
