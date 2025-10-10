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
public class GetSnap extends ServiceConfigDTO {

    @ApiModelProperty(value = "需要截图的url，可以是本机的，也可以是远程主机的，Example : rtsp://www.mym9.com/101065?from=2019-06-28/01:12:13")
    private String url;
    @ApiModelProperty(value = "截图失败超时时间，防止FFmpeg一直等待截图，Example : 10")
    private Integer timeout_sec;
    @ApiModelProperty(value = "截图的过期时间，该时间内产生的截图都会作为缓存返回，Example : 1")
    private Integer expire_sec;

}
