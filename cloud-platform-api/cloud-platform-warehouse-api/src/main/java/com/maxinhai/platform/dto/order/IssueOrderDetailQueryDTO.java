package com.maxinhai.platform.dto.order;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.order.IssueOrderDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("DTO")
public class IssueOrderDetailQueryDTO extends PageSearch<IssueOrderDetailVO> {

    /**
     * 出库单ID
     */
    @ApiModelProperty("出库单ID")
    private String issueOrderId;

}
