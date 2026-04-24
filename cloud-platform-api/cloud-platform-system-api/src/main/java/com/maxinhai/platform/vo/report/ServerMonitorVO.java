package com.maxinhai.platform.vo.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户日增长趋势VO")
public class ServerMonitorVO {

    // Java 版本
    @ApiModelProperty(value = "Java 版本")
    private String javaVersion;
    // CPU 核心数
    @ApiModelProperty(value = "CPU 核心数")
    private Integer cpuCore;
    // 内存总量 GB
    @ApiModelProperty(value = "内存总量 GB")
    private Double memTotal;
    // 内存已用 GB
    @ApiModelProperty(value = "内存已用 GB")
    private Double memUsed;
    // 内存使用率 %
    @ApiModelProperty(value = "内存使用率 %")
    private Integer memUsage;
    // 硬盘总容量 GB
    @ApiModelProperty(value = "硬盘总容量 GB")
    private Double diskTotal;
    // 硬盘已用 GB
    @ApiModelProperty(value = "硬盘已用 GB")
    private Double diskUsed;
    // 硬盘使用率 %
    @ApiModelProperty(value = "硬盘使用率 %")
    private Integer diskUsage;
    // CPU 使用率 %
    @ApiModelProperty(value = "CPU 使用率 %")
    private Integer cpuUsage;
    // JVM 内存使用率 %
    @ApiModelProperty(value = "JVM 内存使用率 %")
    private Integer jvmUsage;
    // 网络带宽使用率 %
    @ApiModelProperty(value = "网络带宽使用率 %")
    private Integer netUsage;

}
