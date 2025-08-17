package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("数据字典新增DTO")
public class WorkCenterEditDTO {

    /**
     * 加工中心编码
     */
    private String code;
    /**
     * 加工中心名称
     */
    private String name;

}
