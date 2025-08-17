package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("数据字典新增DTO")
public class MaterialAddDTO {

    private String code;
    private String name;
    private String materialType;
    private String unit;
    private String description;
    private String specs;
    /**
     * 图号
     */
    private String drawingNo;
    /**
     * 材质
     */
    private String material;
    private String remark;

}
