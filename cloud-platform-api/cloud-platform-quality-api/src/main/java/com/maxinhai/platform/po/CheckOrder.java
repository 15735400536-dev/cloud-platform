package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.enums.CheckType;
import lombok.Data;

@Data
@TableName("qc_check_order")
public class CheckOrder extends RecordEntity {

    /**
     * 检测单编码
     */
    private String orderCode;
    /**
     * 检测模板ID
     */
    private String checkTemplateId;
    /**
     * 检测类型: 自建、互检、专检
     */
    private CheckType checkType;
    /**
     * 产品ID
     */
    private String productId;
    /**
     * 检测结果: 合格、不合格
     */
    private String checkResult;
    /**
     * 检测状态: 0.待检测 1.已检测
     */
    private Integer status;

}
