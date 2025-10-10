package com.maxinhai.platform.dto.technology;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.technology.BomDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "物料BOM明细分页查询DTO")
public class BomDetailQueryDTO extends PageSearch<BomDetailVO> {

    @ApiModelProperty(value = "物料清单ID")
    private String bomId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "父级ID")
    private String parentId;

}
