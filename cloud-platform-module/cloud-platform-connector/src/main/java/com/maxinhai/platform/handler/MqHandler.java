package com.maxinhai.platform.handler;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.bo.MqClientBO;
import com.maxinhai.platform.enums.ConnectType;
import com.maxinhai.platform.mapper.ConnectConfigMapper;
import com.maxinhai.platform.po.ConnectConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName：ApiHandler
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 11:39
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Slf4j
@Component
public class MqHandler implements CommandLineRunner {

    @Resource
    private ConnectConfigMapper connectMapper;

    // 缓存：clientId -> RabbitTemplate（生产者客户端）
    private final Map<String, RabbitTemplate> rabbitTemplateMap = new ConcurrentHashMap<>();
    // 缓存：clientId+topic -> 监听器容器（避免重复注册）
    private final Map<String, SimpleMessageListenerContainer> listenerContainerMap = new ConcurrentHashMap<>();

    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化MQ客户端");
        initClients();
        log.info("MQ客户端初始化完成");

        // 监听消息队列心跳主题
        subscribe("mq", "/heartbeat", msg -> {
            log.info("接收MQ消息:{}", new String(msg.getBody()));
        });
    }

    /**
     * 初始化全部客户端
     */
    public void initClients() {
        List<ConnectConfig> connectList = connectMapper.selectList(new LambdaQueryWrapper<ConnectConfig>()
                .select(ConnectConfig::getId, ConnectConfig::getKey, ConnectConfig::getType,
                        ConnectConfig::getIp, ConnectConfig::getPort,
                        ConnectConfig::getUsername, ConnectConfig::getPassword)
                .eq(ConnectConfig::getType, ConnectType.MQ));
        if (CollectionUtils.isEmpty(connectList)) {
            return;
        }

        for (ConnectConfig connectConfig : connectList) {
            RabbitTemplate rabbitTemplate = initClient(MqClientBO.build(connectConfig));
            rabbitTemplateMap.putIfAbsent(connectConfig.getKey(), rabbitTemplate);
        }
    }

    /**
     * 初始化RabbitMQ客户端
     *
     * @param mqClient MQ客户端配置
     * @return RabbitTemplate
     */
    public RabbitTemplate initClient(MqClientBO mqClient) {
        // 1. 创建连接工厂
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(mqClient.getIp());
        connectionFactory.setPort(mqClient.getPort());
        connectionFactory.setUsername(mqClient.getUsername());
        connectionFactory.setPassword(mqClient.getPassword());
        // 连接池配置：最大空闲连接数（默认2）
        connectionFactory.setConnectionCacheSize(5);
        // 连接超时时间（默认60秒）
        connectionFactory.setConnectionTimeout(30000);
        // 设置虚拟主机（默认"/"）
        connectionFactory.setVirtualHost("/");

        // 2. 创建RabbitTemplate并缓存
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * 断开所有客户端连接（服务关闭时调用）
     */
    @PreDestroy
    public void disconnect() {
        listenerContainerMap.values().forEach(container -> {
            if (container.isRunning()) {
                container.stop();
                log.info("MQ停止监听器容器，关闭消费者线程完成");
            }
        });
    }

    /**
     * 向RabbitMQ发送消息
     *
     * @param clientId 客户端ID
     * @param topic    订阅主题
     * @param message  消息报文
     */
    public void sendMessage(String clientId, String topic, String message) {
        RabbitTemplate rabbitTemplate = rabbitTemplateMap.get(clientId);
        if (rabbitTemplate == null) {
            throw new IllegalArgumentException("RabbitMQ客户端未初始化：" + clientId);
        }
        // RabbitMQ中"topic"对应路由键（需提前绑定交换机和队列）
        rabbitTemplate.convertAndSend(topic, message);
    }

    /**
     * 向RabbitMQ订阅消息
     *
     * @param clientId        客户端ID
     * @param topic           订阅主题
     * @param messageListener 消息监听器
     */
    public void subscribe(String clientId, String topic, MessageListener messageListener) {
        if (rabbitTemplateMap.isEmpty()) {
            throw new IllegalStateException("没有可用的MQ客户端");
        }
        RabbitTemplate rabbitTemplate = rabbitTemplateMap.get(clientId);
        if (rabbitTemplate == null) {
            throw new IllegalArgumentException("RabbitMQ客户端未初始化：" + clientId);
        }
        ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();

        // 构建唯一标识（避免重复注册）
        String cacheKey = clientId + ":" + topic;
        if (listenerContainerMap.containsKey(cacheKey)) {
            return; // 已注册过，直接返回
        }

        // 1. 创建队列（主题对应队列，持久化）
        Queue queue = QueueBuilder.durable(topic) // 队列名=topic
                .withArgument("x-dead-letter-exchange", "") // 可选：死信配置
                .build();

        // 2. 创建交换机（若未指定默认交换机，则使用直连交换机）
        String exchange = rabbitTemplate.getExchange() == null ? "default.direct" : rabbitTemplate.getExchange();
        DirectExchange directExchange = new DirectExchange(exchange, true, false);

        // 3. 绑定队列与交换机（路由键=topic）
        Binding binding = BindingBuilder.bind(queue).to(directExchange).with(topic);

        // 4. 注册队列、交换机、绑定关系（需手动注册到RabbitAdmin）
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.declareQueue(queue);
        admin.declareExchange(directExchange);
        admin.declareBinding(binding);

        // 5. 创建监听器容器
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames(topic);
        container.setMessageListener(new MessageListenerAdapter(new org.springframework.amqp.core.MessageListener() {
            @Override
            public void onMessage(Message message) {
                messageListener.onMessage(message); // 回调处理消息
            }
        }));
        container.start();

        // 6. 缓存监听器容器
        listenerContainerMap.put(cacheKey, container);
    }

    //@Scheduled(initialDelay = 10000, fixedDelay = 5000)
    public void sendMsgTask() {
        JSONObject msg = new JSONObject();
        msg.set("source", "MQ");
        msg.set("type", "heartbeat");
        msg.set("ts", System.currentTimeMillis());
        sendMessage("mq", "/heartbeat", JSONUtil.toJsonStr(msg));
    }

}
