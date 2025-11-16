package com.maxinhai.platform.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.enums.ConnectType;
import com.maxinhai.platform.mapper.ConnectConfigMapper;
import com.maxinhai.platform.mapper.WebSocketConfigMapper;
import com.maxinhai.platform.po.ConnectConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName：ApiHandler
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 11:39
 * @Description: WEBSOCKET消息处理器
 */
@Slf4j
@Component
public class WebSocketHandler implements CommandLineRunner {

    @Resource
    private ConnectConfigMapper connectMapper;
    @Resource
    private WebSocketConfigMapper webSocketMapper;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化WEBSOCKET客户端");
        initClients();
        log.info("WEBSOCKET客户端初始化完成");
    }

    public void initClients() {
        List<ConnectConfig> connectList = connectMapper.selectList(new LambdaQueryWrapper<ConnectConfig>()
                .select(ConnectConfig::getId, ConnectConfig::getType, ConnectConfig::getKey,
                        ConnectConfig::getIp, ConnectConfig::getPort, ConnectConfig::getUsername, ConnectConfig::getPassword)
                .eq(ConnectConfig::getType, ConnectType.WEBSOCKET));
        if (connectList.isEmpty()) {
            return;
        }
    }

}
