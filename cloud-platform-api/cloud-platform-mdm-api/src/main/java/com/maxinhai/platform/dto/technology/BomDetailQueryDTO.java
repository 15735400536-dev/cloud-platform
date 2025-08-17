package com.maxinhai.platform.dto.technology;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.technology.BomDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("DDTO")
public class BomDetailQueryDTO extends PageSearch<BomDetailVO> {

    @ApiModelProperty("物料清单ID")
    private String bomId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("父级ID")
    private String parentId;

}
