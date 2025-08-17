package com.maxinhai.platform.dto.order;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.order.IssueOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("DTO")
public class IssueOrderQueryDTO extends PageSearch<IssueOrderVO> {

    /**
     * 入库单号
     */
    @ApiModelProperty("入库单号")
    private String orderNo;
    /**
     * 仓库ID
     */
    @ApiModelProperty("仓库ID")
    private String warehouseId;

}
