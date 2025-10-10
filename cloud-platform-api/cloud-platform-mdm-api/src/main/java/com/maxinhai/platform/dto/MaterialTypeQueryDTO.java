package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.MaterialTypeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "物料类型分页查询DTO")
public class MaterialTypeQueryDTO extends PageSearch<MaterialTypeVO> {

    @ApiModelProperty(value = "物料类型编码")
    private String code;
    @ApiModelProperty(value = "物料类型名称")
    private String name;
    @ApiModelProperty(value = "父级ID")
    private String parentId;

}
