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
@ApiModel("上传文件VO")
public class UploadFileVO {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("文件名称")
    private String fileName;
    @ApiModelProperty("文件类型")
    private String fileType;
    @ApiModelProperty("文件大小")
    private Long fileSize;
    @ApiModelProperty("文件路径")
    private String filePath;
    @ApiModelProperty("访问地址")
    private String fileUrl;
    @ApiModelProperty("上传时间")
    private Date uploadTime;

    public String getFilePath() {
        return this.filePath;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

}
