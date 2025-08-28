package com.maxinhai.platform.config;

import com.maxinhai.platform.handler.ServerPasswordCallback;
import com.maxinhai.platform.service.DemoService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.xml.ws.Endpoint;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName：WebServiceConfig
 * @Author: XinHai.Ma
 * @Date: 2025/8/28 11:25
 * @Description: WebService配置类
 */
@Configuration
public class WebServiceConfig {

    @Resource
    private Bus bus;

    @Resource
    private DemoService demoService;

    @Resource
    private ServerPasswordCallback serverPasswordCallback;

    // 发布WebService服务
    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, demoService);
        // 服务发布地址
        endpoint.publish("/demoService");
        return endpoint;
    }

    /**
     * 创建WSS4J输入拦截器，配置安全策略
     */
    private WSS4JInInterceptor createWSS4JInInterceptor() {
        Map<String, Object> props = new HashMap<>();
        // 开启用户名令牌认证
        props.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        // 密码类型：明文（实际项目建议使用加密方式）
        props.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        // 认证回调处理器
        props.put(WSHandlerConstants.PW_CALLBACK_REF, serverPasswordCallback);
        return new WSS4JInInterceptor(props);
    }

}
