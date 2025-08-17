package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.enums.CheckType;
import lombok.Data;

@Data
@TableName("qc_check_template")
public class CheckTemplate extends RecordEntity {

    /**
     * 检测模板编码
     */
    private String templateCode;
    /**
     * 检测模板名称
     */
    private String templateName;
    /**
     * 检测类型: 自建、互检、专检
     */
    private CheckType checkType;
    /**
     * 产品ID
     */
    private String productId;
    /**
     * 工序ID
     */
    private String operationId;

}
