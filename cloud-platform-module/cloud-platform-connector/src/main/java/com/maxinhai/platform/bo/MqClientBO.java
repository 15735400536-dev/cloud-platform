package com.maxinhai.platform.bo;

import com.maxinhai.platform.enums.ConnectType;
import com.maxinhai.platform.po.ConnectConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MqClientBO {

    private String key;
    private ConnectType type;
    private String ip;
    private Integer port;
    private String username;
    private String password;
    /**
     * 交换机
     */
    private String exchange;
    /**
     * 订阅主题
     */
    private String topic;

    public static MqClientBO build(ConnectConfig connectConfig) {
        MqClientBO mqClient = new MqClientBO();
        mqClient.setKey(connectConfig.getKey());
        mqClient.setType(connectConfig.getType());
        mqClient.setIp(connectConfig.getIp());
        mqClient.setPort(connectConfig.getPort());
        mqClient.setUsername(connectConfig.getUsername());
        mqClient.setPassword(connectConfig.getPassword());
        return mqClient;
    }

}
