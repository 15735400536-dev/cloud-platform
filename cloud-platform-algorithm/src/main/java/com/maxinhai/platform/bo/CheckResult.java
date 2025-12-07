package com.maxinhai.platform.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "检测结果")
public class CheckResult {

    @ApiModelProperty(value = "人员检测结果：true.检测到 false.未检测到")
    private Boolean personFlag;
    @ApiModelProperty(value = "火灾检测结果：true.检测到 false.未检测到")
    private Boolean fireFlag;
    @ApiModelProperty(value = "睡岗检测结果：true.检测到 false.未检测到")
    private Boolean sleepFlag;
    @ApiModelProperty(value = "声音检测结果：true.检测到 false.未检测到")
    private Boolean soundFlag;
    @ApiModelProperty(value = "缝隙检测结果：true.检测到 false.未检测到")
    private Boolean gapFlag;
    @ApiModelProperty(value = "异物检测结果：true.检测到 false.未检测到")
    private Boolean foreignBodyFlag;
    @ApiModelProperty(value = "破损检测结果：true.检测到 false.未检测到")
    private Boolean damageFlag;
    @ApiModelProperty(value = "指示灯检测结果：true.检测到 false.未检测到")
    private Boolean lightFlag;
    @ApiModelProperty(value = "开关检测结果：true.检测到 false.未检测到")
    private Boolean switchFlag;

}
