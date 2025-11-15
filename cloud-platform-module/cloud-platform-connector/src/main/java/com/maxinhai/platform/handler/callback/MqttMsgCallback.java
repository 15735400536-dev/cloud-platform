package com.maxinhai.platform.handler.callback;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class MqttMsgCallback implements MqttCallback {

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
    }

    /**
     * 消息发布完成时触发（QoS>0时有效）
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
