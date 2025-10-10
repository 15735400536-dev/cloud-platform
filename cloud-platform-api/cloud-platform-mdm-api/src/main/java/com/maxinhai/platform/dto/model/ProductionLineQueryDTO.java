package com.maxinhai.platform.dto.model;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.model.ProductionLineVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "产线分页查询DTO")
public class ProductionLineQueryDTO extends PageSearch<ProductionLineVO> {

    @ApiModelProperty(value = "产线编码")
    private String code;
    @ApiModelProperty(value = "产线名称")
    private String name;
    @ApiModelProperty(value = "车间ID")
    private String workshopId;

}
