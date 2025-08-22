package com.maxinhai.platform.dto.order;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.order.IssueOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "DTO")
public class IssueOrderQueryDTO extends PageSearch<IssueOrderVO> {

    /**
     * 入库单号
     */
    @ApiModelProperty(value = "入库单号")
    private String orderNo;
    /**
     * 仓库ID
     */
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;

}
