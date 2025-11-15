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
@TableName("conn_mqtt")
public class MqttConfig extends RecordEntity {

    /**
     * 连接ID
     */
    private String connectId;
    /**
     * 订阅主题
     */
    private String topic;
    /**
     * QoS等级（0/1/2）
     */
    private Integer qos;

}
