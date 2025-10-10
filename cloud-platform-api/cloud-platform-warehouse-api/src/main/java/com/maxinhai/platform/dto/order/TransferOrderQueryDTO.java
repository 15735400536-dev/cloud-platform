package com.maxinhai.platform.dto.order;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.order.TransferOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "移库单分页查询DTO")
public class TransferOrderQueryDTO extends PageSearch<TransferOrderVO> {

    /**
     * 移库单号
     */
    @ApiModelProperty(value = "移库单号")
    private String transferNo;
    /**
     * 源仓库ID
     */
    @ApiModelProperty(value = "源仓库ID")
    private String sourceWarehouseId;
    /**
     * 源仓库ID
     */
    @ApiModelProperty(value = "源仓库ID")
    private String targetWarehouseId;

}
