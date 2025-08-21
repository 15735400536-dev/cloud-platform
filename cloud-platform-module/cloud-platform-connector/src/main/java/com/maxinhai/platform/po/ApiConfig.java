package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName：ApiConfig
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 10:39
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@TableName("api_config")
public class ApiConfig extends RecordEntity {

    private String connectId;
    private String method;
    private String url;

}
