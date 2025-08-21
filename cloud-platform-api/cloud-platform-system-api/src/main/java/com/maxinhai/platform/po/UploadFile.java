package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName：UploadFile
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 14:16
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_upload_file")
public class UploadFile extends RecordEntity{

    private String fileName;
    private String fileType;
    private Long fileSize;
    private String filePath;
    private String fileUrl;
    private Date uploadTime;

}
