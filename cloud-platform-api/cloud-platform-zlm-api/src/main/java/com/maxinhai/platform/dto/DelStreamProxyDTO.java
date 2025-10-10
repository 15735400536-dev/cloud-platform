package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "关闭拉流代理DTO")
public class DelStreamProxyDTO extends ServiceConfigDTO {

    @ApiModelProperty(value = "addStreamProxy接口返回的key")
    private String key;

}
