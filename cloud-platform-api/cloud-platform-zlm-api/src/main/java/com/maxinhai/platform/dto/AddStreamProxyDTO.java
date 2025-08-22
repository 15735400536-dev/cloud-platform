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
@ApiModel(description = "获取截图DTO")
public class AddStreamProxyDTO extends ServiceConfigDTO {

    @ApiModelProperty(value = "添加的流的虚拟主机，例如defaultVhost")
    private String vhost = "__defaultVhost__";
    @ApiModelProperty(value = "添加的流的应用名，例如live")
    private String app;
    @ApiModelProperty(value = "添加的流的id名，例如test")
    private String stream;
    @ApiModelProperty(value = "拉流地址，例如rtmp://live.hkstv.hk.lxdns.com/live/hks2")
    private String url;

}
