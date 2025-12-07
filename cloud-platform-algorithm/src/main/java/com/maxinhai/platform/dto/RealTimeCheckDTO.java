package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "实时检测DTO")
public class RealTimeCheckDTO {

    @ApiModelProperty(value = "检测图片集合(Base64编码)")
    private List<String> images;

}
