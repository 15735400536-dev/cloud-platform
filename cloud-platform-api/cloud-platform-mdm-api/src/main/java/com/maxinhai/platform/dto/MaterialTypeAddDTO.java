package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("数据字典新增DTO")
public class MaterialTypeAddDTO {

    private String code;
    private String name;
    private String description;
    private String parentId;

}
