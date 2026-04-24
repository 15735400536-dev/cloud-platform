package com.maxinhai.platform.po.technology;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

@Data
@TableName("mdm_operation_file")
public class OperationFile extends RecordEntity {

    /**
     * 工序ID
     */
    private String operationId;
    /**
     * 工艺文件名称
     */
    private String fileName;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * 文件保存路径
     */
    private String filePath;
    /**
     * 文件访问地址
     */
    private String fileUrl;

}
