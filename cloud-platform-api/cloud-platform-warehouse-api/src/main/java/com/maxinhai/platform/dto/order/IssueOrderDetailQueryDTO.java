package com.maxinhai.platform.dto.order;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.order.IssueOrderDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "入库单明细分页查询DTO")
public class IssueOrderDetailQueryDTO extends PageSearch<IssueOrderDetailVO> {

    /**
     * 出库单ID
     */
    @ApiModelProperty(value = "出库单ID")
    private String issueOrderId;

}
