package com.maxinhai.platform.handler.callback;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class MqttMsgCallback implements MqttCallback {

    // 存储：topic -> 该topic对应的消息队列（线程安全的Map + 阻塞队列）
    public static final Map<String, Queue<MqttMessage>> mqttMessagesMap = new ConcurrentHashMap<>();
    // 队列容量（根据业务需求调整，避免过小导致消息丢失）
    private static final int QUEUE_CAPACITY = 1000;

    /**
     * 连接丢失时触发（可实现重连逻辑）
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.error("MQTT连接断开，原因: {}", throwable.getMessage(), throwable);
    }

    /**
     * 接收消息时触发
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
        log.info("收到消息 - 主题: {}, 内容: {}", topic, payload);
        mqttMessagesMap.putIfAbsent(topic, new ArrayBlockingQueue<>(QUEUE_CAPACITY, true));
    }

    /**
     * 消息发布完成时触发（QoS>0时有效）
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
