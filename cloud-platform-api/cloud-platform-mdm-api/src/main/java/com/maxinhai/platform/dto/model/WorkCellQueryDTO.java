package com.maxinhai.platform.dto.model;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.model.WorkCellVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工位分页查询DTO")
public class WorkCellQueryDTO extends PageSearch<WorkCellVO> {

    @ApiModelProperty(value = "工位编码")
    private String code;
    @ApiModelProperty(value = "工位名称")
    private String name;
    /**
     * 加工中心ID
     */
    @ApiModelProperty(value = "加工中心ID")
    private String workCenterId;
    /**
     * 产线ID
     */
    @ApiModelProperty(value = "产线ID")
    private String productionLineId;

}
