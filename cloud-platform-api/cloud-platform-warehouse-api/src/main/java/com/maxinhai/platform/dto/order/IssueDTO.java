package com.maxinhai.platform.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName：IssueDTO
 * @Author: XinHai.Ma
 * @Date: 2025/8/20 19:45
 * @Description: 出库DTO
 */
@Data
@ApiModel(description = "出库DTO")
public class IssueDTO {

    @ApiModelProperty(value = "出库单ID")
    private String issueOrderId;
    @ApiModelProperty(value = "出库单明细")
    private List<IssueDetailDTO> issueDetailList;

}
