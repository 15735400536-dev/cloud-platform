package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.MaterialTypeVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("数据字典新增DTO")
public class MaterialTypeQueryDTO extends PageSearch<MaterialTypeVO> {

    private String code;
    private String name;
    private String parentId;

}
