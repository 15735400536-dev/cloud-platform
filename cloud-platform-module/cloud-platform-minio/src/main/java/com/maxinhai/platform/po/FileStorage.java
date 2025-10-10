package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @ClassName：FileStorage
 * @Author: XinHai.Ma
 * @Date: 2025/9/12 10:58
 * @Description: 文件存储
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_file_storage")
public class FileStorage extends RecordEntity {

    private String fileName;       // 原始文件名
    private String originalName;   // 原始文件名
    private String suffix;         // 文件后缀
    private String contentType;    // 文件类型
    private Long fileSize;         // 文件大小(字节)
    private String filePath;       // 文件存储路径
    private String url;            // 访问URL
    private LocalDateTime uploadTime; // 上传时间
    private String uploader;       // 上传者

}
