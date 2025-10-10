package com.maxinhai.platform.client;

import com.maxinhai.platform.handler.ServerPasswordCallback;
import com.maxinhai.platform.po.Demo;
import com.maxinhai.platform.service.DemoService;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName：WebServiceClientTest
 * @Author: XinHai.Ma
 * @Date: 2025/8/28 11:29
 * @Description: 测试用例
 */
@SpringBootTest
public class WebServiceClientTest {

    @Resource
    private ServerPasswordCallback serverPasswordCallback;

    @Test
    public void testWebService() {
        // 创建代理工厂
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();

        // 设置服务地址
        factory.setAddress("http://localhost:20011/ws/demoService?wsdl");

        // 设置服务接口
        factory.setServiceClass(DemoService.class);

        // 创建服务代理
        DemoService demoService = (DemoService) factory.create();

        // 调用服务方法
        Demo demo = demoService.getDemoById("1");
        System.out.println("查询Demo: " + demo);

        // 测试添加Demo
        Demo newDemo = new Demo("3", "王五", "28");
        boolean result = demoService.addDemo(newDemo);
        System.out.println("添加Demo结果: " + (result ? "成功" : "失败"));

        // 验证添加结果
        Demo addedDemo = demoService.getDemoById("3");
        System.out.println("新增Demo: " + addedDemo);
    }

    @Test
    public void testSecureWebService() {
        // 创建代理工厂
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();

        // 设置服务地址
        factory.setAddress("http://localhost:20011/ws/demoService?wsdl");

        // 设置服务接口
        factory.setServiceClass(DemoService.class);

        // 创建服务代理
        DemoService demoService = (DemoService) factory.create();

        // 添加安全拦截器，设置用户名密码
        Client client = ClientProxy.getClient(demoService);
        client.getOutInterceptors().add(createWSS4JOutInterceptor("admin", "123456"));

        try {
            // 调用服务方法
            Demo demo = demoService.getDemoById("1");
            System.out.println("查询Demo: " + demo);

            // 测试添加Demo
            Demo newDemo = new Demo("3", "王五", "28");
            boolean result = demoService.addDemo(newDemo);
            System.out.println("添加Demo结果: " + (result ? "成功" : "失败"));
        } catch (Exception e) {
            System.out.println("调用失败: " + e.getMessage());
        }
    }

    /**
     * 创建WSS4J输出拦截器，添加用户名密码认证信息
     */
    private WSS4JOutInterceptor createWSS4JOutInterceptor(String username, String password) {
        Map<String, Object> props = new HashMap<>();

        // 开启用户名令牌认证
        props.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);

        // 密码类型：明文
        props.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);

        // 用户名
        props.put(WSHandlerConstants.USER, username);

        // 密码
        props.put(WSHandlerConstants.PW_CALLBACK_REF, serverPasswordCallback);

        return new WSS4JOutInterceptor(props);
    }

}
