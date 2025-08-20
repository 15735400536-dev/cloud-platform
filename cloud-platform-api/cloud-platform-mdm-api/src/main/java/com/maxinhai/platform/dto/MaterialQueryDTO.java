package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.MaterialVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("物料新增DTO")
public class MaterialQueryDTO extends PageSearch<MaterialVO> {

    @ApiModelProperty("物料编码")
    private String code;
    @ApiModelProperty("物料名称")
    private String name;

}
