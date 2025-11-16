package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName：ApiConfig
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 10:39
 * @Description: WEB_SOCKET配置详情
 */
@Data
@TableName("conn_websocket")
public class WebSocketConfig extends RecordEntity {

    private String connectId;
    private String topic;

}
