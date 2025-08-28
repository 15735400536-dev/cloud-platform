package com.maxinhai.platform.handler;

import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

/**
 * @ClassName：ServerPasswordCallback
 * @Author: XinHai.Ma
 * @Date: 2025/8/28 11:38
 * @Description: 处理WebService认证的回调处理器
 */
@Component
public class ServerPasswordCallback implements CallbackHandler {

    @Value("${password:123456}")
    private String password;

    /**
     * 这里可以从数据库或配置文件中获取合法的用户名密码
     * 实际项目中建议使用加密存储和验证
     */
    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        WSPasswordCallback callback = (WSPasswordCallback) callbacks[0];

        // 模拟验证：用户名密码正确则通过认证
        if ("admin".equals(callback.getIdentifier())) {
            // 设置预期的密码，WSS4J会自动与客户端传来的密码比对
            callback.setPassword(password);
        } else {
            // 用户名不存在，会抛出认证失败异常
            throw new SecurityException("认证失败：用户名或密码错误");
        }
    }

}
