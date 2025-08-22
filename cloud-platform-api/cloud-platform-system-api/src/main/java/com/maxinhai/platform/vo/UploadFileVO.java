package com.maxinhai.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName：UploadFileVO
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 14:26
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel(description = "上传文件VO")
public class UploadFileVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "文件名称")
    private String fileName;
    @ApiModelProperty(value = "文件类型")
    private String fileType;
    @ApiModelProperty(value = "文件大小")
    private Long fileSize;
    @ApiModelProperty(value = "文件路径")
    private String filePath;
    @ApiModelProperty(value = "访问地址")
    private String fileUrl;
    @ApiModelProperty(value = "上传时间")
    private Date uploadTime;

    public String getFilePath() {
        return this.filePath;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

}
