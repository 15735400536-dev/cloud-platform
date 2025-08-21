package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.UploadFileVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @ClassName：UploadFileQueryDTO
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 14:25
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel("上传文件分页查询DTO")
public class UploadFileQueryDTO extends PageSearch<UploadFileVO>{

    private String fileName;
    private String fileType;

}
