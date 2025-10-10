package com.maxinhai.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ServiceConfigDTO {

    /**
     * IP地址
     */
    protected String ip;
    /**
     * 端口
     */
    protected int port;
    /**
     * 密钥
     */
    protected String secret;

    /**
     * 设置父级服务配置
     * @param target
     * @param source
     */
    public static void setConfig(ServiceConfigDTO target, ServiceConfigDTO source) {
        target.setIp(source.getIp());
        target.setPort(source.getPort());
        target.setSecret(source.getSecret());
    }

}
