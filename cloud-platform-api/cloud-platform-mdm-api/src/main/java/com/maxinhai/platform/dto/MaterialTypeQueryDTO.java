package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.MaterialTypeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("物料类型分页查询DTO")
public class MaterialTypeQueryDTO extends PageSearch<MaterialTypeVO> {

    @ApiModelProperty("物料类型编码")
    private String code;
    @ApiModelProperty("物料类型名称")
    private String name;
    @ApiModelProperty("父级ID")
    private String parentId;

}
