package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.enums.MediaType;
import com.maxinhai.platform.enums.Method;
import lombok.Data;

/**
 * @ClassName：ApiConfig
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 10:39
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@TableName("conn_api")
public class ApiConfig extends RecordEntity {

    private String connectId;
    private String apiKey;
    private Method method;
    private String url;
    private MediaType mediaType;

}
